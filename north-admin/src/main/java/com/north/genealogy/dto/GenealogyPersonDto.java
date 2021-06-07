package com.north.genealogy.dto;

import com.north.genealogy.entity.GenealogyPerson;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-04-23
 */
public class GenealogyPersonDto extends GenealogyPerson {

    private String parentId;

    private String partnerId;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GenealogyPersonDto.class.getSimpleName() + "[", "]")
                .add("parentId='" + parentId + "'")
                .add("partnerId='" + partnerId + "'")
                .toString();
    }
}
