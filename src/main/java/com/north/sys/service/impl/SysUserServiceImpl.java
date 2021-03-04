package com.north.sys.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.base.exception.LoginFailedException;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.mapper.SysUserMapper;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

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
    public SysUser checkUserLogin(String usernaem, String password) {
        password = MD5.create().digestHex(password, StandardCharsets.UTF_8);
        LambdaQueryWrapper<SysUser> qw = Wrappers.lambdaQuery();
        qw.eq(SysUser::getUsername, usernaem);
        qw.eq(SysUser::getPassword, password);
        SysUser sysUser = this.getOne(qw, false);
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
}
