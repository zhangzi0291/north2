package com.north.aop.validator;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 参数校验
 *
 * @author North
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateParam {

    /**
     * 校验规则
     *
     * @return
     */
    ValidatorEnum value();

    /**
     * 额外参数
     *
     * @return
     */
    String express() default "";

    /**
     * 需要校验的字段名
     *
     * @return
     */
    String parameterName();

    /**
     * 校验失败的返回文本
     *
     * @return
     */
    String message() default "";

}
