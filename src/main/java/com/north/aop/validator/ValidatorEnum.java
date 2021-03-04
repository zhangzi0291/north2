package com.north.aop.validator;

import java.util.function.BiFunction;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-23
 */
public enum ValidatorEnum {

    IS_NULL("参数必须为NULL", ValidateUtil::IsNull),
    NOT_NULL("参数必须不为NULL", ValidateUtil::NotNull),
    NOT_EMPTY("参数必须不为空", ValidateUtil::NotEmpty),
    ASSERT_TRUE("参数必须为true", ValidateUtil::AssertTrue),
    ASSERT_FLASE("参数必须为false", ValidateUtil::AssertFalse),
    Past("参数必须是一个过去的日期", ValidateUtil::Past),
    Future("参数必须是一个将来的日期", ValidateUtil::Future),
    Range("参数必须在合适的范围内", ValidateUtil::Range),
    Length("参数长度必须在指定范围内", ValidateUtil::Length),
    IS_INTEGER("参数必须是整数", ValidateUtil::IsInteger),
    IS_DIGITS("参数必须是数字", ValidateUtil::IsDigits),
    Pattern("参数必须符合指定的正则表达式", ValidateUtil::Pattern);;

    public String msg;

    /**
     * BiFunction：接收字段值(Object)和 表达式(String)，返回是否符合规则(Boolean)
     */
    public BiFunction<Object, String, Boolean> fun;

    ValidatorEnum(String msg, BiFunction<Object, String, Boolean> fun) {
        this.msg = msg;
        this.fun = fun;
    }

}
