package com.north.genealogy.dto;

import java.util.StringJoiner;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-04-22
 */
public class RelationshipInfo {

    private String genealogyId;

    private String parentId;

    private String partnerId;

    public String getGenealogyId() {
        return genealogyId;
    }

    public void setGenealogyId(String genealogyId) {
        this.genealogyId = genealogyId;
    }

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
        return new StringJoiner(", ", RelationshipInfo.class.getSimpleName() + "[", "]")
                .add("genealogyId='" + genealogyId + "'")
                .add("parentId='" + parentId + "'")
                .add("partnerId='" + partnerId + "'")
                .toString();
    }
}
