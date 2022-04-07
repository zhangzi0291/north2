package com.north.sys.dto;

import java.io.Serializable;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-22
 */
public class SelectFieldDto implements Serializable {

    private String lable;

    private Object value;

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SelectFieldDto{" +
                "lable='" + lable + '\'' +
                ", value=" + value +
                '}';
    }
}
