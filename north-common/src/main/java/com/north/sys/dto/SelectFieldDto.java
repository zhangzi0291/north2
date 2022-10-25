package com.north.sys.dto;

import java.io.Serializable;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-22
 */
public class SelectFieldDto implements Serializable {

    private String label;

    private Object value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
                "label='" + label + '\'' +
                ", value=" + value +
                '}';
    }
}
