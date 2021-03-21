package com.north.sys.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.api.R;
import com.north.constant.DeviceTypeEnum;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-30
 */
@Tag(name = "sys-login-controller 登陆相关")
@RestController
@RequestMapping("sysLogin")
public class SysLoginController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysLogService sysLogService;
    /**
     * 登陆
     *
     * @param username
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */

    @NorthWithoutLogin
    @Operation(summary = "登陆", description = "登陆")
    @RequestMapping(path = "login", method = RequestMethod.POST)
    @ValidateParams(
            @ValidateParam(value = ValidatorEnum.ENUM_CLASS,parameterName="deviceType",express = "DeviceTypeEnum")
    )
    public R login(String username, String password, String deviceType) {
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkCanUserLogin(username, password);
        //获取角色返回
        List<String> roles = sysUserService.getRoleList(sysUser.getId());
        //获取权限返回
        List<String> permissions = sysUserService.getPermissionLis(sysUser.getId());

        //登录
        sysUserService.login(sysUser, DeviceTypeEnum.WEB.value);

        Map<String, Object> result = new HashMap<>();
        sysUser.setPassword(null);
        result.put("user", sysUser);
        result.put("token", StpUtil.getTokenValue());
        result.put("roles", roles);
        result.put("permissions", permissions);
        return R.ok(result);
    }

    /**
     * 注销
     *
     * @return
     */
    @Operation(summary = "注销", description = "退出登陆")
    @RequestMapping(path = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public R logout(String deviceType) {
        sysUserService.logout(deviceType);
        return R.ok();
    }
}
