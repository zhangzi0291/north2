package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色菜单关联
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@TableName("sys_role_resource")
public class SysRoleResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 资源ID
     */
    private String resourceId;

    /**
     * 描述
     */
    private String describe;

    /**
     * 是否删除
     */
    @JsonIgnore
    private Integer isDeleted;

    /**
     * 乐观锁
     */
    @JsonIgnore
    private Integer revision;

    /**
     * 创建人
     */
    @JsonIgnore
    private String createdBy;

    /**
     * 创建时间
     */
    @JsonIgnore
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    @JsonIgnore
    private String updatedBy;

    /**
     * 更新时间
     */
    @JsonIgnore
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "SysRoleResource{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", resourceId=" + resourceId +
                ", describe=" + describe +
                ", isDeleted=" + isDeleted +
                ", revision=" + revision +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", updatedBy=" + updatedBy +
                ", updateTime=" + updateTime +
                "}";
    }
}
