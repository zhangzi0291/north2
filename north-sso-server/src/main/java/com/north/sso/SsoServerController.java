package com.north.sso;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.sso.SaSsoConsts;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.base.api.R;
import com.north.constant.DeviceTypeEnum;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-08-19
 */
@NorthWithoutLogin
@RequestMapping("sso-server")
@RestController
public class SsoServerController {

    @Value("${sa-token.sso.not-login-url}")
    private String notLoginUrl;
    @Resource
    private ISysUserService sysUserService;

    /**
     * 当前是否登录
     *
     * @return
     */
    @RequestMapping("/isLogin")
    public R isLogin() {
        return R.ok(StpUtil.isLogin() + "");
    }

    /**
     * 返回SSO认证中心登录地址
     *
     * @param redirect 跳转地址
     * @param hash     跳转地址的定位hash
     * @return
     */
    @RequestMapping("/getSsoAuthUrl")
    public R getSsoAuthUrl(HttpServletRequest request, HttpServletResponse response, String redirect, String hash) {
        String redirectUrl = redirect;
        String serverAuthUrl = SaSsoUtil.buildServerAuthUrl(redirectUrl, "");
        if (StringUtils.hasLength(hash)) {
            serverAuthUrl += "&hash=" + hash;
        }
        return R.ok(serverAuthUrl);
    }

    /**
     * 根据ticket进行登录
     *
     * @param ticket
     * @return
     */
    @RequestMapping("/doLoginByTicket")
    public R doLoginByTicket(String ticket, String redirect) {
        Object loginId = SaSsoUtil.checkTicket(ticket);
        if (loginId != null) {
            SysUser sysUser = sysUserService.getById(loginId.toString());
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
            result.put("redirect", redirect);

            return R.ok(result);
        }
        return R.failed("无效ticket：" + ticket);
    }

    /**
     * 单点认证，如果已经登录跳转回原始地址，未登录跳转到认证中心前端，认证中心地址sa-token.sso.not-login-url中定义
     *
     * @param redirect
     * @param hash
     * @return
     */
    @RequestMapping("/auth")
    public Object ssoAuth(String redirect, String hash) {
        // 获取对象
        SaRequest req = SaHolder.getRequest();
        SaResponse res = SaHolder.getResponse();
        SaSsoConfig cfg = SaManager.getConfig().getSso();
        StpLogic stpLogic = SaSsoUtil.saSsoTemplate.stpLogic;

        // ---------- 此处有两种情况分开处理：
        // ---- 情况1：在SSO认证中心尚未登录，需要先去登录
        if (stpLogic.isLogin() == false) {
            return res.redirect(notLoginUrl + "?redirect=" + redirect + "&hash=" + hash);
        }
        // ---- 情况2：在SSO认证中心已经登录，需要重定向回 Client 端，而这又分为两种方式：
        String mode = req.getParam(SaSsoConsts.ParamName.mode, "");

        // 方式1：直接重定向回Client端 (mode=simple)
        if (mode.equals(SaSsoConsts.MODE_SIMPLE)) {
            SaSsoUtil.checkRedirectUrl(redirect);
            return res.redirect(redirect + "#" + hash);
        } else {
            // 方式2：带着ticket参数重定向回Client端 (mode=ticket)
            // 校验 重定向地址 是否合法
            SaSsoUtil.checkRedirectUrl(redirect);
            // 删掉 旧Ticket
            SaSsoUtil.deleteTicket(SaSsoUtil.saSsoTemplate.getTicketValue(stpLogic.getLoginId()));
            // 创建 新Ticket
            String ticket = SaSsoUtil.createTicket(stpLogic.getLoginId());

            return doLoginByTicket(ticket, redirect + "#" + hash);
        }
    }

    /**
     * 单点登录接口
     *
     * @param username
     * @param password
     * @param redirect
     * @param hash
     * @return
     */
    @RequestMapping("/ssoLogin")
    public Object ssoLogin(String username, String password, String redirect, String hash) {
        SaRequest req = SaHolder.getRequest();
        SaResponse res = SaHolder.getResponse();
        SaSsoConfig cfg = SaManager.getConfig().getSso();
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkCanUserLogin(username, password);

        sysUserService.login(sysUser, DeviceTypeEnum.WEB.getValue(), 2592000L);

        return res.redirect(cfg.authUrl + "?redirect=" + redirect + "&hash=" + hash);
    }

    /**
     * 单点注销
     *
     * @param loginId   用户id
     * @param secretkey 密钥，sa-token.sso.secretkey中定义
     * @return
     */
    @RequestMapping("/ssoLogout")
    public Object ssoLogout(String loginId, String secretkey) {
        // 获取对象
        SaRequest req = SaHolder.getRequest();
        SaSsoConfig cfg = SaManager.getConfig().getSso();
        StpLogic stpLogic = SaSsoUtil.saSsoTemplate.stpLogic;
        // 遍历通知Client端注销会话
        // step.1 校验秘钥
        SaSsoUtil.checkSecretkey(secretkey);
        // step.2 遍历通知Client端注销会话
        SaSsoUtil.forEachSloUrl(loginId, url -> cfg.sendHttp.apply(url));
        // step.3 Server端注销
        stpLogic.logout(loginId);
        // 完成
        return R.ok();
    }


}
