package com.north.aop.permissions;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.north.base.Constant;
import com.north.sys.entity.SysResource;
import com.north.sys.service.ISysResourceService;
import com.north.utils.SpringUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-30
 */
public class NorthCheckPermissionsInterceptor implements HandlerInterceptor {

    private boolean VERIFY = true;

    public StpLogic stpLogic;

    public NorthCheckPermissionsInterceptor() {
        this(StpUtil.stpLogic);
    }

    public NorthCheckPermissionsInterceptor(StpLogic stpLogic) {
        this(stpLogic, true);
    }

    public NorthCheckPermissionsInterceptor(boolean verify) {
        this(StpUtil.stpLogic, verify);
    }

    public NorthCheckPermissionsInterceptor(StpLogic stpLogic, boolean verify) {
        this.stpLogic = null;
        this.stpLogic = stpLogic;
        this.VERIFY = verify;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!VERIFY) {
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        } else {
            HandlerMethod method = (HandlerMethod) handler;
            //有NorthWithoutLogin不需要登陆
            if (method.hasMethodAnnotation(NorthWithoutLogin.class) || method.getBeanType().isAnnotationPresent(NorthWithoutLogin.class)) {
                return true;
            }
            //检查是否登陆
            this.stpLogic.checkLogin();
            //admin用户不校验权限
            if (StpUtil.getLoginIdAsString().equals("1")) {
                return true;
            }
            NorthWithoutPermissions nwp = method.getMethodAnnotation(NorthWithoutPermissions.class);
            if (nwp == null) {
                nwp = method.getBeanType().getAnnotation(NorthWithoutPermissions.class);
            }
            //有NorthWithoutPermissions的时候不校验权限
            if (nwp != null) {
                return true;
            } else {
                String url = request.getRequestURI();
                if (StringUtils.hasLength(Constant.CONTEXT_PATH)) {
                    url = request.getRequestURI().replace(Constant.CONTEXT_PATH, "");
                }
                SysResource resource = SpringUtil.getBean(ISysResourceService.class).getSysResourceByUrl(url);
                if (resource == null) {
                    return true;
                }
                StpUtil.checkPermissionOr(new String[]{resource.getResourceName()});
                return true;
            }
        }
    }


}
