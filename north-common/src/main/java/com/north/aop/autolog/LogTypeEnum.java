package com.north.aop.autolog;

public enum LogTypeEnum {

    LOGIN("登录", 1),
    LOGOUT("注销", 2),
    BUSINESS("业务", 3),
    ;

    private String name;
    private Integer value;

    LogTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
