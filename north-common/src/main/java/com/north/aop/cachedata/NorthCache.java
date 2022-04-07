package com.north.aop.cachedata;

import com.north.base.Constant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NorthCache {

    /**
     * 缓存的key
     *
     * @return
     */
    String key() default Constant.NORTH_CACHE_REDIS_PREFIX;

    /**
     * 缓存时间
     * @return
     */
    int timeout() default 5;

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;
}
