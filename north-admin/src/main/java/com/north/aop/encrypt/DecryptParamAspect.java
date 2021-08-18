package com.north.aop.encrypt;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.north.base.Constant;
import com.north.base.api.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(DecryptParamAspect.class);

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
            try {
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
            } catch (Exception e) {
                logger.error("解密失败",e);
                return R.failed(parameter.getName()+"字段解密失败");
            }
        }
        return joinPoint.proceed(args);
    }

}
