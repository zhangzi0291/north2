package com.north.sys.controller;

import cloud.tianai.captcha.slider.SliderCaptchaApplication;
import cloud.tianai.captcha.vo.CaptchaResponse;
import cloud.tianai.captcha.vo.SliderCaptchaVO;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.Constant;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.constant.DeviceTypeEnum;
import com.north.msg.service.IMsgService;
import com.north.msg.service.impl.EmailMsgService;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysRoleService;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import com.north.utils.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private SysUserController sysUserController;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private Map<String, IMsgService> msgService;
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private SliderCaptchaApplication sliderCaptchaApplication;
    @Resource
    private RedissonClient redissonClient;

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
        //保存用户信息
        sysUser.setAppName("web");

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
    @NorthWithoutLogin
    @Operation(summary = "登陆", description = "登陆")
    @RequestMapping(path = "login", method = RequestMethod.POST)
    @ValidateParams(
            @ValidateParam(value = ValidatorEnum.ENUM_CLASS, parameterName = "deviceType", express = "DeviceTypeEnum")
    )
    public R login(String genId,String username, String password, String deviceType) {
        //检查验证码
        sysUserService.checkGen(genId);
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkCanUserLogin(username, password);
        //获取角色返回
        List<String> roles = sysUserService.getRoleList(sysUser.getId());
        //获取权限返回
        List<String> permissions = sysUserService.getPermissionLis(sysUser.getId());

        //登录
        sysUserService.login(sysUser, DeviceTypeEnum.WEB.getValue(), 2592000L);

        Map<String, Object> result = new HashMap<>();
        sysUser.setPassword(null);
        result.put("user", sysUser);
        result.put("token", StpUtil.getTokenValue());
        result.put("roles", roles);
        result.put("permissions", permissions);
        return R.ok(result);
    }

    /**
     * 通过Email获取验证码
     *
     * @param target
     * @return
     */
    @NorthWithoutLogin
    @RequestMapping(path = "getVerificationCodeWithEmail", method = RequestMethod.GET)
    public R getVerificationCodeWithEmail(String target) {
        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        qw.lambda().eq(SysUser::getEmail, target);
        SysUser sysUser = sysUserService.getOne(qw, false);
        if (sysUser == null) {
            return R.failed("用户不存在");
        }
        String VerificationCode = sysUserService.createVerificationCode(sysUser.getId(), 15);
        msgService.get(StrUtil.lowerFirst(EmailMsgService.class.getSimpleName())).sendMsg(Arrays.asList(new String[]{target}), "North验证码", VerificationCode);
        return R.ok(VerificationCode);
    }

    /**
     * 校验email对应的验证码
     *
     * @param target
     * @param verificationCode
     * @return
     */
    @NorthWithoutLogin
    @RequestMapping(path = "loginByVerificationCodeWithEmail", method = RequestMethod.POST)
    public R loginByVerificationCodeWithEmail(String target, String verificationCode) {
        QueryWrapper<SysUser> qw = new QueryWrapper<>();
        qw.lambda().eq(SysUser::getEmail, target);
        SysUser sysUser = sysUserService.getOne(qw, false);
        if (sysUser == null) {
            return R.failed("用户不存在");
        }
        boolean b = sysUserService.checkVerificationCode(sysUser.getId(), verificationCode);
        if (b) {
            if (sysUser != null) {
                sysUserService.login(sysUser, DeviceTypeEnum.WEB.getValue());
                //获取角色返回
                List<String> roles = sysUserService.getRoleList(sysUser.getId());
                //获取权限返回
                List<String> permissions = sysUserService.getPermissionLis(sysUser.getId());

                Map<String, Object> result = new HashMap<>();
                sysUser.setPassword(null);
                result.put("user", sysUser);
                result.put("token", StpUtil.getTokenValue());
                result.put("roles", roles);
                result.put("permissions", permissions);
                return R.ok(result);
            }
        }
        return R.failed("验证码错误");
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

    @NorthWithoutLogin
    @Operation(summary = "滑块验证码", description = "滑块验证码")
    @RequestMapping(path = "gen", method = {RequestMethod.GET, RequestMethod.POST})
    public CaptchaResponse<SliderCaptchaVO> gen(HttpServletRequest request) {
        CaptchaResponse<SliderCaptchaVO> response = sliderCaptchaApplication.generateSliderCaptcha();
        return response;
    }

    @NorthWithoutLogin
    @Operation(summary = "滑块验证", description = "滑块验证")
    @RequestMapping(path = "check", method = {RequestMethod.GET, RequestMethod.POST})
    public R check(@RequestParam("id") String id,
                                @RequestParam("percentage") Float percentage,
                                HttpServletRequest request) {
        Boolean flag = sliderCaptchaApplication.matching(id, percentage);
        if(flag){
            redissonClient.getBucket(Constant.REDIS_PREFIX+id).set(true,5, TimeUnit.MINUTES);
        }
        return R.ok(flag.toString());
    }

}
