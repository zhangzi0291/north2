package com.north.genealogy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

/**
 * <p>
 * 族谱-父子树
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@TableName("genealogy_parent_tree")
public class GenealogyParentTree extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 族谱ID
     */
    private String genealogyId;

    /**
     * 子ID
     */
    private String personId;

    /**
     * 父ID
     */
    private String parentId;

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
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "GenealogyParentTree{" +
            "genealogyId=" + genealogyId +
            ", personId=" + personId +
            ", parentId=" + parentId +
        "}";
    }
}
