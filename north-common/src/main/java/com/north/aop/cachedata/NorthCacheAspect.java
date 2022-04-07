package com.north.aop.cachedata;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.north.base.Constant;
import com.north.base.api.R;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2022-04-07
 */
@Aspect
@Component
public class NorthCacheAspect {

    private static final String KEY = "dLW/oqjru0eIL4PWNZxsPQ==";

    private static final SymmetricCrypto AES_CRYPTO = new SymmetricCrypto(SymmetricAlgorithm.AES, Base64.getDecoder().decode(KEY));

    @Resource
    private RedissonClient redissonClient;

    @Pointcut(value = "@annotation(com.north.aop.cachedata.NorthCache)")
    public void NorthCache() {
    }

    @Around("NorthCache()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        NorthCache northCache = AnnotationUtils.getAnnotation(targetMethod,NorthCache.class);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String keys = getKyes(joinPoint,attributes.getRequest());

        RBucket cache = redissonClient.getBucket(northCache.key() +keys);
        if(cache.isExists()){
            try {
                return R.ok(JSONObject.parse(AES_CRYPTO.decryptStr(cache.get().toString())));
            }catch (ClassCastException e){
                return R.ok(JSONArray.parse(AES_CRYPTO.decryptStr(cache.get().toString())));
            }
        }

        R r = (R)joinPoint.proceed();
        cache.set( AES_CRYPTO.encryptBase64(JSON.toJSONString(r.getData())) );
        cache.expire(northCache.timeout(), northCache.timeUnit());
        return r;
    }

    /**
     * 入参数据
     * @param joinPoint
     * @param request
     * @return
     */
    private String getKyes(ProceedingJoinPoint joinPoint,HttpServletRequest request) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Annotation[] annotations = targetMethod.getAnnotations();
        Map map = request.getParameterMap();
        String jsonStr = JSON.toJSONString(map);
        String md5 = MD5.create().digestHex(jsonStr);
        return md5;
    }

}
