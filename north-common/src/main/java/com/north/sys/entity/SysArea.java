package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * <p>
 *
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-04
 */
@TableName("sys_area")
public class SysArea extends DeleteLogicBaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 父区域ID
     */
    private String parentId;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 区域级别
     */
    private Integer areaLevel;

    /**
     * 树结构ID
     */
    private String treeId;

    /**
     * 排序
     */
    private Integer areaOrder;

    /**
     * 城乡分类代码
     */
    private String townCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public String getTreeId() {
        return treeId;
    }

    public void setTreeId(String treeId) {
        this.treeId = treeId;
    }

    public Integer getAreaOrder() {
        return areaOrder;
    }

    public void setAreaOrder(Integer areaOrder) {
        this.areaOrder = areaOrder;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysArea.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("parentId='" + parentId + "'")
                .add("areaName='" + areaName + "'")
                .add("areaLevel=" + areaLevel)
                .add("treeId='" + treeId + "'")
                .add("areaOrder=" + areaOrder)
                .add("townCode='" + townCode + "'")
                .toString();
    }
}
