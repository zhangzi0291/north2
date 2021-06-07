package com.north.file.dto;

public enum FileControlType {

    //本地存储
    LOCAL(1,"local"),

    //OSS存储
    OSS(2,"oss"),
    ;

    public Integer value;

    public String name;

    FileControlType(Integer value,String name){
        this.value = value;
        this.name = name;
    }

    public static FileControlType getEnumByValue(Integer value) {
        for (FileControlType eEnum : values()) {
            if (eEnum.value.equals(value)) {
                return eEnum;
            }
        }
        return null;
    }
}
