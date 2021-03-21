package com.north.constant;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-21
 */
public enum DeviceTypeEnum {

    /**
     * PC网页
     */
    WEB("web"),
    /**
     * 手机
     */
    MOBILE("mobile"),
    ;

    public String value;

    DeviceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
