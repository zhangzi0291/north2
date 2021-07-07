package com.north.pytool.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.AES;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.aop.encrypt.DecryptParam;
import com.north.aop.encrypt.EncryptResponseBody;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.Constant;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.constant.DeviceTypeEnum;
import com.north.sys.controller.SysUserController;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysRoleService;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import com.north.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-07-05
 */
@Tag(name = "small-tool-login-controller 小工具登陆相关")
@RestController
@RequestMapping("smallTool")
public class SmallToolController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private SysUserController sysUserController;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    /**
     * 注册
     *
     * @param sysUser
     * @param roleIds
     * @return
     */
    @NorthWithoutLogin
    @Transactional
    @Operation(summary = "注册", description = "注册")
        @RequestMapping(path = "register", method = RequestMethod.POST)
    @ValidateParams({
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "sysUser.username"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "sysUser.password"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "sysUser.nickname"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "sysUser.phone"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "sysUser.email"),
    })
    public R register(SysUser sysUser, @RequestParam(value = "roleIds", required = false) List<String> roleIds) {
        //校验用户名是否重复
        if (!sysUserService.checkUsername(sysUser.getUsername())) {
            return R.failed("用户名重复");
        }
        //两次MD5编码
        sysUser.setPassword(MD5.create().digestHex(sysUser.getPassword(), StandardCharsets.UTF_8));
        sysUser.setPassword(PasswordUtil.encodePassword(sysUser.getPassword()));
        sysUser.setAppName("py小工具");
        //保存用户信息
        if (!sysUserService.checkUsername(sysUser.getUsername())) {
            return R.failed("用户名重复");
        }
        R r = sysUserController.addJson(sysUser);
        if (ApiErrorCode.SUCCESS.getCode() != r.getCode()) {
            return r;
        }

        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapper<SysRole> qw = Wrappers.query();
            qw.lambda().in(SysRole::getId, roleIds);
            if (sysRoleService.list(qw).size() > 0) {
                return R.failed("角色不存在");
            }
            //保存用户和角色的关联信息
            sysUserRoleService.addUserRole(sysUser.getId(), roleIds);
        }
        return R.ok();
    }

    /**
     * 登陆
     *
     * @param username
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */

    @EncryptResponseBody
    @NorthWithoutLogin
    @Operation(summary = "登陆", description = "登陆")
    @RequestMapping(path = "sys/login", method = RequestMethod.POST)
    @ValidateParams({
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "username"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "password"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "softVersino"),
    })
    public R login(String username, @DecryptParam String password, String softVersino) {
        //校验软件版本
        if (!Constant.PY_SMALL_TOOL_SOFTVERSION.equals(softVersino)) {
            return R.failed(ApiErrorCode.CheckSoftVersionFieldError, "不是最新版本，请更新");
        }
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkCanUserLogin(username, password);
        //获取角色返回
        List<String> roles = sysUserService.getRoleList(sysUser.getId());
        //获取权限返回
        List<String> permissions = sysUserService.getPermissionLis(sysUser.getId());

        //登录,保持30天
        sysUserService.login(sysUser, DeviceTypeEnum.PY_SMALL_TOOL.getValue(), 2592000L);

        Map<String, Object> result = new HashMap<>();
        sysUser.setPassword(null);
        result.put("user", sysUser);
        result.put("tokenInfo", StpUtil.getTokenInfo());
        result.put("roles", roles);
        result.put("permissions", permissions);
        return R.ok(result);
    }

    public static void main(String[] args) {
//        String text = "hP4V6pgH35NRqyNK0TSYUQ==";
        String text = "2iGSkV2Rq9em8FCBP2+SI5Wy0Z9Kw9epj2Xx91+w7aowIidgDMVEtodQ/EXhxYwCNwlT1dphz5O7x4ig0xdNncAyoICSNnb89Uu5KnAHPMMxAADt15iS5wm2tbg0/ub91wP0/yArJlKo95nzJl/YKszcRjmDfB24CeywxnT/Mx+IzcbLAWWt4SAssUBidEAXDLG+8KRtU1WwvU1Wm/ngbRGT4jyOaSTyot3MQBy10y0TDo78c5JknkxL/TRg/4eWwWQmyxQZNrU362bVFv7tPzNjnIaFxTbs5PZ8sN0PGm0QHzYsG3gyeVglhsC0BLCSRp3s6B0qoAjRMZF4qo4ktO62/ytdwTOlpAyqm6ZjbjMoVLv1XJdyaEjeP5OXGoaE1es+XLQ1fw7Xnq0shhwCjuDlLqDbYIRoJZ9AAMSS9E+t/I7y0dNhVj6tgSSb2818y45Biir3spHpdLqELgrFgKZ6+5OoxsnjH/nqH00oEYfVfMe4wuGe58mP/sz3zRAc2BTqw71u2fEq9TfJcdhK91Euo9oO2WAyPMQRcAB9efwEalyJA6So5NyIpGNA1Sof+VII24gujsEzPKeGbv22MXL4lQKGcqx31YmMv73c/r1S5nKUzFt4Qu1xnGP6yxxmd6i/SV2jkQ2bYfn0hCa1+1TwSRf4AzWBgaa3nOocpxFOIaDaCSyyhfVZgGrZXhTvckdbEj7j9oPDMwO2cmLLnz1hDqn/JNS3Yd0XLhcvI3nPGmHV7LusAaKTFVGlDr/0T6B/VAgJfTW6ozYd/jCPRqiYpoO6mmppLCvEVi6bXbM=";


        String key = "howsoaestpyrcnef";
        String iv = "8145933630441549";
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding,key.getBytes(StandardCharsets.UTF_8),iv.getBytes(StandardCharsets.UTF_8));
        String r = aes.decryptStr(text);
        System.out.println(r);
    }
}
