package com.north.sys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.base.Constant;
import com.north.base.exception.impl.LoginExceptionEnum;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.entity.SysUserRole;
import com.north.sys.mapper.SysUserMapper;
import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import com.north.utils.PasswordUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Resource
    private ISysLogService sysLogService;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @Override
    public List<SysRole> getUserRole(String userId) {
        return baseMapper.getUserRole(userId);
    }

    @Override
    public List<SysResource> getUserResource(String userId) {
        return baseMapper.getUserResource(userId);
    }

    @Override
    public SysUser checkCanUserLogin(String usernaem, String password) {
        password = PasswordUtil.encodePassword(password);
        LambdaQueryWrapper<SysUser> qw = Wrappers.lambdaQuery();
        qw.eq(SysUser::getUsername, usernaem);
        qw.eq(SysUser::getPassword, password);
        SysUser sysUser = this.getOne(qw, false);
        //用户不存在
        LoginExceptionEnum.USER_PWD_ERROR.assertNonNull(sysUser);
        //用户过期
        LoginExceptionEnum.EXPIRED_ERROR.assertFalse(sysUser.getExpiredTime() != null && LocalDateTime.now().compareTo(sysUser.getExpiredTime()) > 0);
        //用户锁定
        LoginExceptionEnum.LOCKING_ERROR.assertFalse(sysUser.getStatus() != null && sysUser.getStatus().equals(0));
        return sysUser;
    }

    @Override
    public Boolean checkGen(String genId) {
        LoginExceptionEnum.GEN_ERROR.assertTrue(Boolean.TRUE.equals(redissonClient.getBucket(Constant.NORTH_GEN_REDIS_PREFIX +genId).get()));

        redissonClient.getBucket(Constant.NORTH_GEN_REDIS_PREFIX +genId).deleteAsync();
        return Boolean.TRUE;
    }

    @Override
    public void login(SysUser user, String deviceType) {
        login(user, deviceType, null);
    }

    @Override
    public void login(SysUser user, String deviceType, Long timeout) {
        //敏感信息去除
        user.setPassword(null);
        user.setEmail(null);
        user.setPhone(null);
        //登陆
        StpUtil.login(user.getId(), deviceType);
        if (timeout != null) {
            StpUtil.setTokenValue(StpUtil.getTokenValue(), timeout.intValue());
        }
        //设置session里的参数
        StpUtil.getSession().set("username", user.getUsername());
        StpUtil.getSession().set("nickname", user.getNickname());
        StpUtil.getSession().set("userId", user.getId());
        if (StringUtils.hasLength(user.getIconUrl())) {
            StpUtil.getSession().set("iconUrl", user.getIconUrl());
        }
        StpUtil.getSession().set("loginDevice", StpUtil.getLoginDevice());
        //更新最后登陆时间
        {
            SysUser u = new SysUser();
            u.setId(user.getId());
            u.setLastLoginTime(LocalDateTime.now());
            this.updateById(u);
        }
        //记录登陆日志
        sysLogService.addLoginLog();
    }

    @Override
    public void logout(String deviceType) {
        sysLogService.addLogoutLog();
        StpUtil.kickout(StpUtil.getLoginId(),deviceType);
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        password = PasswordUtil.encodePassword(password);
        SysUser user = this.getById(userId);
        return user.getPassword().equals(password);
    }

    @Override
    public Boolean checkUsername(String username) {
        List<SysUser> list = this.lambdaQuery().eq(SysUser::getUsername, username).select(SysUser::getId).list();
        return list.size() <= 0;
    }

    @Override
    public Boolean checkNickname(String nickname) {
        List<SysUser> list = this.lambdaQuery().eq(SysUser::getNickname, nickname).select(SysUser::getId).list();
        return list.size() <= 0;
    }

    @Override
    public Long getTotalOnlineNum() {
        RKeys rKeys = redissonClient.getKeys();
        Iterable<String> keys = rKeys.getKeysByPattern("satoken:login:session:*");
        Long total = 0L;
        for (String key : keys) {
            total += 1;
        }
        return total;
    }

    @Override
    public List<String> getPermissionLis(String userId) {
        List<SysResource> roles = getUserResource(userId);
        List<String> list = roles.stream().map(SysResource::getResourceName).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<String> getRoleList(String userId) {
        List<SysRole> roles = getUserRole(userId);
        List<String> list = roles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        return list;
    }

    @Override
    public String createVerificationCode(String userId, Integer invalidTime) {
//        String verificationCode = RandomUtil.getRandom(RandomUtil.NUMBERS.toCharArray(), 6);
        String verificationCode = "123456";
        RBucket<String> bucket = redissonClient.getBucket(userId);
        bucket.set(verificationCode);
        bucket.expire(invalidTime, TimeUnit.MINUTES);
        return verificationCode;
    }

    @Override
    public Boolean checkVerificationCode(String key, String verificationCode) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        String value = bucket.get();
        if (value != null && value.equals(verificationCode)) {
            bucket.delete();
            return true;
        }
        return false;
    }

    @Override
    public void importSysUserList(List<Map<String, Object>> list) {
        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        for (Map<String, Object> som : list) {
            SysUser sysUser = (SysUser) som.get("user");
            List<SysRole> sysRoleList = (List<SysRole>) som.get("roleList");
            this.save(sysUser);
            for (SysRole sysRole : sysRoleList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(sysUser.getId());
                sysUserRole.setRoleId(sysRole.getId());
                sysUserRoleList.add(sysUserRole);
            }
        }
        sysUserRoleService.saveBatch(sysUserRoleList);
    }

}
