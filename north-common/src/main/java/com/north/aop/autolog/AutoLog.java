package com.north.aop.autolog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoLog {

    LogTypeEnum logType();

    ModuleNameEnum moduleName();

    String operationName();
}
