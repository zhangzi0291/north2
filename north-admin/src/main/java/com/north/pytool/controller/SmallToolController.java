package com.north.pytool.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.MD5;
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
import com.north.msg.service.impl.EmailMsgService;
import com.north.pytool.service.IPytoolAppService;
import com.north.sys.controller.SysUserController;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysRoleService;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import com.north.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-07-05
 */
@Tag(name = "small-tool-login-controller 小工具登陆相关")
@NorthWithoutLogin
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
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private EmailMsgService emailMsgService;
    @Resource
    private IPytoolAppService pytoolAppService;

    /**
     * 注册
     *
     * @param sysUser
     * @param roleIds
     * @return
     */
    @Transactional
    @Operation(summary = "注册", description = "注册")
    @RequestMapping(path = "sys/register", method = RequestMethod.POST)
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
    @Operation(summary = "登陆", description = "登陆")
    @RequestMapping(path = "sys/login", method = RequestMethod.POST)
    @ValidateParams({
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "username"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "password"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "softVersino"),
    })
    public R login(String username, @DecryptParam String password, String softVersino,String softName) {

        //校验软件版本
        if (!Constant.PY_SMALL_TOOL_SOFTVERSION.equals(softVersino)) {
            return R.failed(ApiErrorCode.CheckSoftVersionFieldError, "不是最新版本，请更新");
        }
        //校验软件名称
        if (!Constant.PY_SMALL_TOOL_SOFTNAME.equals(softName)) {
            return R.failed(ApiErrorCode.CheckSoftVersionFieldError, "不存在的软件，请更新");
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

    @EncryptResponseBody
    @Operation(summary = "登陆", description = "登陆")
    @RequestMapping(path = "sys/loginv2", method = RequestMethod.POST)
    @ValidateParams({
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "username"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "password"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "softVersion"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "softName"),
    })
    public R loginv2(String username, @DecryptParam String password, String softVersion,String softName) {
        //校验软件版本
        if (!pytoolAppService.checkSoft(softName,softVersion)) {
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

    @Operation(summary = "修改密码", description = "不需要登录")
    @RequestMapping(path = "sys/changePassword", method = RequestMethod.POST)
    public R changePasswordWithoutLogin(String key, String username, String password, String oldPassword) {
        if(!StringUtils.hasLength(key)){
            return R.failed("不可修改");
        }
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!StringUtils.hasLength(bucket.get())) {
            return R.failed("不可修改");
        }
        if(!bucket.get().equals(username)){
            return R.failed("用户名不匹配");
        }
        QueryWrapper<SysUser> qw = Wrappers.query();
        qw.lambda().eq(SysUser::getUsername, username);
        SysUser user = sysUserService.getOne(qw);
        R r = sysUserController.changePassword(user.getId(), password, oldPassword);
        if (ApiErrorCode.SUCCESS.getCode() == r.getCode()) {
            bucket.delete();
        }
        return r;
    }

    @Operation(summary = "发送修改密码的邮件", description = "")
    @RequestMapping(path = "sys/sendChangePassword", method = RequestMethod.POST)
    public R sendChangePassword(String username) {
        QueryWrapper<SysUser> qw = Wrappers.query();
        qw.lambda().eq(SysUser::getUsername, username);
        SysUser user = sysUserService.getOne(qw);
        if (user == null) {
            R.failed("用户不存在");
        }
        String key = UUID.randomUUID().toString();
        //15分钟内可以修改密码
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(username);
        bucket.expire(15, TimeUnit.MINUTES);

        String url = Constant.PY_SMALL_TOOL_WEB_URL + "/pytool/changepwd?key=" + key;
        String[] sendList = new String[]{user.getEmail()};
        emailMsgService.sendMsg(Arrays.asList(sendList.clone()), "修改密码", "修改密码地址：" + url +" \r\n 地址15分钟内有效");
        return R.ok("发送成功，15分钟内可以修改密码");
    }
}
