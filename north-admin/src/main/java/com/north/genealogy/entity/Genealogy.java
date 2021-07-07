package com.north.genealogy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.BaseModel;

/**
 * <p>
 * 族谱
 * </p>
 *
 * @author NorthZX
 * @since 2021-04-14
 */
@TableName("genealogy")
public class Genealogy extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 族谱名称
     */
    private String genealogyName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenealogyName() {
        return genealogyName;
    }

    public void setGenealogyName(String genealogyName) {
        this.genealogyName = genealogyName;
    }

    @Override
    public String toString() {
        return "Genealogy{" +
                "id=" + id +
                ", genealogyName=" + genealogyName +
                "}";
    }
}
