package com.north.aop.encrypt;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.north.base.Constant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;

/**
 * DecryptParam注解的AOP实现类
 *
 * @author Northzx
 * @version 1.0
 * @since 2021-07-07
 */
@Aspect
@Component
public class DecryptParamAspect {


    @Pointcut(value = "within(com.north..controller..*)")
    public void access() {

    }

    @Around("access()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取参数对象
        Object[] args = joinPoint.getArgs();
        //获取方法参数
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            DecryptParam decryptParam = parameter.getAnnotation(DecryptParam.class);
            if (decryptParam != null) {
                Object value = args[i];
                if (value == null) {
                    continue;
                }
                System.out.println(value);
                AES aes = new AES(Mode.CBC, Padding.NoPadding, Constant.AES_KEY.getBytes(StandardCharsets.UTF_8), Constant.AES_IV.getBytes(StandardCharsets.UTF_8));
                String resultStr = aes.decryptStr(value.toString());
                args[i] = resultStr.trim();
            }
        }
        return joinPoint.proceed(args);
    }

}
