package com.north.el.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.north.aop.encrypt.EncryptResponseBody;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.api.R;
import com.north.constant.DeviceTypeEnum;
import com.north.el.entity.EtAppInfo;
import com.north.sys.controller.SysUserController;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysUserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/etLogin")
public class EtLoginController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private EtAppInfoController etAppInfoController;

    /**
     * 登陆
     *
     * @param username
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */
    @NorthWithoutLogin
    @EncryptResponseBody
    @Operation(summary = "登陆", description = "登陆")
    @RequestMapping(path = "login", method = {RequestMethod.POST, RequestMethod.GET})
    @ValidateParams(
            @ValidateParam(value = ValidatorEnum.ENUM_CLASS, parameterName = "deviceType", express = "com.north.constant.DeviceTypeEnum")
    )
    public R login(String genId, String username, String password, String deviceType) {
        //检查验证码
        sysUserService.checkGen(genId);
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkCanUserLogin(username, password);

        Long timeout = 1000L * 60L * 60L * 24L * 7L;
        //登录
        sysUserService.login(sysUser, DeviceTypeEnum.ELECTRON_TOOL.getValue(), timeout);

        R<List<EtAppInfo>> app = etAppInfoController.getAppByUserId(sysUser.getId());
        Map<String, Object> result = new HashMap<>();
        sysUser.setPassword(null);
        result.put("user", sysUser);
        result.put("token", StpUtil.getTokenValue());
        result.put("app", app.getData());
        result.put("timeout", timeout);
        return R.ok(result);
    }

    public static void main(String[] args) {
        System.out.println(new Date(1660471289650L));
    }
}

