package com.north.sys.dto;

import com.north.sys.entity.SysResource;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-06-15
 */
public class SysResourceDto extends SysResource {

    /**
     * 父元素名称
     */
    private String parentName;


    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String toString() {
        return super.toString() + new StringJoiner(", ", SysResourceDto.class.getSimpleName() + "[", "]")
                .add("parentName='" + parentName + "'");
    }
}
