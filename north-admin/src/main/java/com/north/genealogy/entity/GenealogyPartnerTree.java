package com.north.genealogy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

/**
 * <p>
 * 族谱-配偶树
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@TableName("genealogy_partner_tree")
public class GenealogyPartnerTree extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 族谱ID
     */
    private String genealogyId;

    /**
     * ID
     */
    private String personId;

    /**
     * 配偶ID
     */
    private String partnerId;

    public String getGenealogyId() {
        return genealogyId;
    }

    public void setGenealogyId(String genealogyId) {
        this.genealogyId = genealogyId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String toString() {
        return "GenealogyPartnerTree{" +
                "genealogyId=" + genealogyId +
                ", personId=" + personId +
                ", partnerId=" + partnerId +
                "}";
    }
}
