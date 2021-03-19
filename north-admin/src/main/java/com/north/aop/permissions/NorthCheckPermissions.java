package com.north.aop.permissions;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NorthCheckPermissions {

    @AliasFor("url")
    String value() default "";

    boolean check() default true;

    String url() default "";
}
