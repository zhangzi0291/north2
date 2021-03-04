package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.north.base.BaseModel;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * <p>
 * 组织
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@TableName("sys_org")
public class SysOrg extends BaseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 父ID
     */
    private String parentId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 所属地市
     */
    private String areaId;

    /**
     * 排序
     */
    private Integer orgOrder;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Integer getOrgOrder() {
        return orgOrder;
    }

    public void setOrgOrder(Integer orgOrder) {
        this.orgOrder = orgOrder;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysOrg.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("parentId='" + parentId + "'")
                .add("orgName='" + orgName + "'")
                .add("areaId='" + areaId + "'")
                .add("orgOrder=" + orgOrder)
                .add("describe='" + describe + "'")
                .add("isDeleted=" + isDeleted)
                .add("revision=" + revision)
                .add("createdBy='" + createdBy + "'")
                .add("createdTime=" + createdTime)
                .add("updatedBy='" + updatedBy + "'")
                .add("updateTime=" + updateTime)
                .toString();
    }
}
