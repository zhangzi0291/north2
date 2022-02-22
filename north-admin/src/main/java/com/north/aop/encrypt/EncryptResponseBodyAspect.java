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

    @Pointcut(value = "@annotation(com.north.aop.encrypt.EncryptResponseBody) & com.north.base.api.R com.north..*.*(..)")
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
        if(result.getData()!=null) {
            AES aes = new AES(Mode.CBC, Padding.ZeroPadding, Constant.AES_KEY.getBytes(StandardCharsets.UTF_8), Constant.AES_IV.getBytes(StandardCharsets.UTF_8));
            String resultBase64 = aes.encryptBase64(JSON.toJSONString(result.getData()));
            result.setData(resultBase64);
        }
        return result;
    }

    public static void main(String[] args) {
        AES aes = new AES(Mode.CBC, Padding.ZeroPadding, "jiannanchuntuand".getBytes(StandardCharsets.UTF_8), "8145933630441549".getBytes(StandardCharsets.UTF_8));
        String s = aes.decryptStr("cUqaMHXawfGz3CWQhfBq5gu69fMz82I1rwoB/kUhYosmiNz+aUJ/GIWGPU7+OuI7lMLXGb1nnCF4/g07Z+i8EbeK/az2mT69uhYh+lQMAnyz9gycaeTU+Tvo5DCdZogKlI31fQmMI9zwx0+v/uX60K8H3q01exh5RvF+SCxaTjEQBxigDUvNs77s65qigYXTNHmnC9+1QG2/YUCnujaSGMVw4DK59n3UYNK1x8fZdIveRZpGrZhMeWaxZ/4l/AiR5ZPC/JF2VJdhSJ20PslIk6HzGHbLEdacPsKqQo7yPUSr9DXbiZY31Vtgw8FoAKN0Zej2KAyWkdxIFVJ54Jcik/1MJc3TqqJnxEVj+NnMPb/v7a6q0hGF+pZeAXT9LdFd6J4VgcJPI8aWeXjB+72tSK3lAzmvykcbN3o4hkbKh2mtMkjCzegEGjwHiSvoEp/o8IjSdEdCaOft3XMy52+fL3ormr8+yQ2XiqflO7cItQh+5G/WDMwHo0N/fV90lCpkwv3qsY3Bg14FSIFB9AJwtxpKlT9EA+3AHonuVTgfslQU/orYtqYmnLVSFh/8fpPPIHEZYkyArMtYbQLgZPaRQ6CFpTFiOXXiqvFRW2Db9cOVsZ8ZaAcUkbJqQN/ElA0aZoSeeyjm8AmVagpEw42Y7N4lwBje0bv+vqQfy5UJPaJikUHNmlonakmh3WJMd+oIoPMucwGr9JkQLIoqI3mrIy4YTS2bKn7sTj8ce7F7xwZAbfawxTvoQzNxYxeYfw0ppaSyzwREdVI9iYr85v25quaWr4Dx1JdMb5SjmjxiWWaoUvXCyAYIabGv/U0OiW0RfsqPN5SqlGgX9J4LYejD/WXc8UJsIrXuubKJht0PUMb/ibfbI+HBYv4eh3aUUYn8SjuGXYMnjc1WFUJh/Nv19ZrkMEtNlt/9JIRxKkBhKA650CcsKk34uJ8+jVQgSYjLVoxgxI8c15IVsiiQ61l5s8KIE1Tvv/XwmRYMUodsxQrHrlUGpx97nPxohP6DaeRlJAuRKIwF0OYOFeFIY7+LWlgixywpjOAljE6ArBgkL1AjphaGOUnBzp2ilrZXnboDakMkv19rvjBE9MmEhLo1gw==");
        System.out.println(s);
    }
}
