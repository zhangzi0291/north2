package com.north.sys.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.MD5;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.base.api.R;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
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
    public R login(String username, String password) {
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkUserLogin(username, password);
        //获取角色返回
        List<SysRole> roles = sysUserService.getUserRole(sysUser.getId());
        Map<String, Object> result = new HashMap<>();
        sysUser.setPassword(null);
        result.put("user", sysUser);
        result.put("roles", roles);
        //记录登陆的userId
        StpUtil.setLoginId(sysUser.getId());

        return R.ok(result);
    }

    /**
     * 注销
     *
     * @return
     */
    @Operation(summary = "注销", description = "退出登陆")
    @RequestMapping(path = "logout", method = {RequestMethod.GET, RequestMethod.POST})
    public R logout() {
        StpUtil.logout();
        return R.ok();
    }
}
