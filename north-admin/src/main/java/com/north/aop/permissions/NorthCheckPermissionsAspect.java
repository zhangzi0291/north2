package com.north.aop.permissions;

import cn.dev33.satoken.stp.StpUtil;
import com.north.base.Constant;
import com.north.sys.entity.SysRole;
import com.north.sys.service.ISysRoleService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-30
 */
@Aspect
@Component
public class NorthCheckPermissionsAspect {

    @Resource
    private ISysRoleService sysRoleService;

    @Pointcut(value = "@annotation(com.north.aop.permissions.NorthCheckPermissions)")
    public void access() {

    }

    @Before("access()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURI();
        if (StringUtils.hasLength(Constant.CONTEXT_PATH)) {
            url = request.getRequestURI().replace(Constant.CONTEXT_PATH, "");
        }

        List<SysRole> roles = sysRoleService.getSysRoleByUrl(url);
        List<String> roleStrs = roles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        StpUtil.checkPermissionOr(roleStrs.toArray(new String[roleStrs.size()]));

    }
}
