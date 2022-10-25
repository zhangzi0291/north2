package com.north.sso;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaSsoConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.sso.SaSsoConsts;
import cn.dev33.satoken.sso.SaSsoHandle;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
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

    @Value("${north.sso.serverHost}")
    private String ssoServerHost;

    @Value("${north.sso.loginPageUrl}")
    private String loginPageUrl;

    private static final String authUrl="/sso-server/auth";

    private static final String logoutUrl="/sso-server/logout";

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
        String serverUrl = ssoServerHost + authUrl;
        String serverAuthUrl = SaFoxUtil.joinParam(serverUrl, SaSsoConsts.ParamName.redirect, redirectUrl);
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
        StpLogic stpLogic = SaSsoUtil.saSsoTemplate.stpLogic;

        // ---------- 此处有两种情况分开处理：
        // ---- 情况1：在SSO认证中心尚未登录，需要先去登录
        if (stpLogic.isLogin() == false) {
            return res.redirect(loginPageUrl + "?redirect=" + redirect + "&hash=" + hash);
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
        String serverUrl = ssoServerHost + authUrl;
        //检查密码是否可以登陆
        SysUser sysUser = sysUserService.checkCanUserLogin(username, password);

        sysUserService.login(sysUser, DeviceTypeEnum.WEB.getValue());

        return res.redirect(serverUrl + "?redirect=" + redirect + "&hash=" + hash);
    }

    /**
     * 单点注销
     *
     * @return
     */
    @RequestMapping("/ssoLogout")
    public Object ssoLogout() {
        SaSsoHandle.ssoLogoutByUserVisit();
        // 完成
        return R.ok();
    }


}
