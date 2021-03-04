package com.north.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-02-24
 */
public class BaseModel implements Serializable {

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

}
