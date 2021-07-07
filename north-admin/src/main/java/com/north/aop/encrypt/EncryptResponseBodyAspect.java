package com.north.aop.encrypt;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.north.base.Constant;
import com.north.base.api.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * EncryptResponseBody注解的AOP实现类
 *
 * @author Northzx
 * @version 1.0
 * @since 2021-07-07
 */
@Aspect
@Component
public class EncryptResponseBodyAspect {

    @Pointcut(value = "@annotation(com.north.aop.encrypt.EncryptResponseBody)")
    public void access() {

    }

    @Around("access()")
    public R doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        //执行被注解方法,需要retrun proceed()的返回值
        R result = null;
        try {
            result = (R) joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding, Constant.AES_KEY.getBytes(StandardCharsets.UTF_8), Constant.AES_IV.getBytes(StandardCharsets.UTF_8));
//        System.out.println(aes.encryptBase64(JSON.toJSONString(result)));
//        System.out.println(aes.encryptHex(JSON.toJSONString(result)));

        String resultBase64 = aes.encryptBase64(JSON.toJSONString(result.getData()));
        result.setData(resultBase64);
        return result;
    }
}
