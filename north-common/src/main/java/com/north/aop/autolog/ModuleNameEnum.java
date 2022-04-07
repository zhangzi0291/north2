package com.north.aop.autolog;

public enum ModuleNameEnum {

    LOGIN("登录", 1),
    SYSTEM("系统", 2),
    ;

    private final String name;
    private final Integer value;

    ModuleNameEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
