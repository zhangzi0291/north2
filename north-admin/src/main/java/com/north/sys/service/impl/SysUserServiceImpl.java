package com.north.sys.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.base.exception.LoginFailedException;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.entity.SysUserRole;
import com.north.sys.mapper.SysUserMapper;
import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
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
        password = MD5.create().digestHex(password, StandardCharsets.UTF_8);
        LambdaQueryWrapper<SysUser> qw = Wrappers.lambdaQuery();
        qw.eq(SysUser::getUsername, usernaem);
        qw.eq(SysUser::getPassword, password);
        SysUser sysUser = this.getOne(qw, false);
        RSet rset = redissonClient.getSet("");
        //用户不存在
        if (sysUser == null) {
            throw LoginFailedException.newInstance(LoginFailedException.LoginFailedEnum.USER_PWD_ERROR);
        }
        //用户过期
        if (sysUser.getExpiredTime() != null && LocalDateTime.now().compareTo(sysUser.getExpiredTime()) > 0) {
            throw LoginFailedException.newInstance(LoginFailedException.LoginFailedEnum.EXPIRED_ERROR);
        }
        //用户锁定
        if (sysUser.getStatus() != null && sysUser.getStatus().equals(0)) {
            throw LoginFailedException.newInstance(LoginFailedException.LoginFailedEnum.LOCKING_ERROR);
        }
        return sysUser;
    }

    @Override
    public void login(SysUser user, String deviceType) {
        StpUtil.setLoginId(user.getId());
        StpUtil.getSession().setAttribute("nickname", user.getNickname());
        StpUtil.getSession().setAttribute("userId", user.getId());
        //记录登陆日志
        sysLogService.addLoginLog();
    }

    @Override
    public void logout(String deviceType) {
        sysLogService.addLogoutLog();
        StpUtil.logoutByLoginId(deviceType);
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        password = MD5.create().digestHex(password, StandardCharsets.UTF_8);
        SysUser user = this.getById(userId);
        return user.getPassword().equals(password);
    }

    @Override
    public Boolean checkUsername(String username) {
        List<SysUser> list = this.lambdaQuery().eq(SysUser::getUsername, username).select(SysUser::getId).list();
        if (list.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean checkNickname(String nickname) {
        List<SysUser> list = this.lambdaQuery().eq(SysUser::getNickname, nickname).select(SysUser::getId).list();
        if (list.size() > 0) {
            return false;
        }
        return true;
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
