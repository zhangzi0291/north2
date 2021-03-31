package com.north.aop.autolog;

public enum ModuleNameEnum {

    LOGIN("登录", 1),
    SYSTEM("系统", 2),
    ;

    private String name;
    private Integer value;

    ModuleNameEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
}
