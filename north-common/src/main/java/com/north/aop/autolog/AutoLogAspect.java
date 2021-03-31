package com.north.aop.autolog;

import cn.dev33.satoken.stp.StpUtil;
import com.north.sys.entity.SysLog;
import com.north.sys.service.ISysLogService;
import com.north.utils.IpUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-09
 */
@Aspect
@Component
public class AutoLogAspect {

    @Resource
    private ISysLogService sysLogService;

    @Pointcut(value = "@annotation(com.north.aop.autolog.AutoLog)")
    public void access() {

    }

    @Around("access()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //开始时间
        long start = System.currentTimeMillis();

        String fullName = joinPoint.getSignature().toString();//类名和方法
        String className = fullName.substring(0, fullName.lastIndexOf("."));
        String methodName = fullName.substring(fullName.lastIndexOf(".") + 1);

        SysLog log = new SysLog();
        log.setLogType(1);
        log.setUserId(StpUtil.getLoginIdAsString());
        String nickname = StpUtil.getSession().getDataMap().getOrDefault("nickname", "").toString();
        log.setNickname(nickname);
        log.setIpAddr(IpUtil.getIp(request));
        log.setModuleName("test");
        log.setOperationName("test");
        log.setClassName(className);
        log.setMethodName(methodName);

        //执行被注解方法,需要retrun proceed()的返回值
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            sysLogService.save(log);
            throw throwable;
        }
        //结束时间
        long end = System.currentTimeMillis();
        long useTime = end - start;
        log.setUseTime(useTime);
        sysLogService.save(log);
        return result;
    }

}
