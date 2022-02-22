package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.LogicDeleteBaseModel;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@TableName("sys_resource")
public class SysResource extends LogicDeleteBaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源类型
     */
    private Integer resourceType;

    /**
     * 资源路径
     */
    private String resourceUrl;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 资源图标
     */
    private String resourceIcon;

    /**
     * 排序
     */
    private Integer resourceOrder;

    /**
     * 描述
     */
    private String describe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public Integer getResourceOrder() {
        return resourceOrder;
    }

    public void setResourceOrder(Integer resourceOrder) {
        this.resourceOrder = resourceOrder;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysResource.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("resourceName='" + resourceName + "'")
                .add("resourceType=" + resourceType)
                .add("resourceUrl='" + resourceUrl + "'")
                .add("parentId='" + parentId + "'")
                .add("resourceIcon='" + resourceIcon + "'")
                .add("resourceOrder=" + resourceOrder)
                .add("describe='" + describe + "'")
                .toString();
    }
}
