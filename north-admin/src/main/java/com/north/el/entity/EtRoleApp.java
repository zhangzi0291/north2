package com.north.el.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

/**
 * <p>
 * 角色和应用关联
 * </p>
 *
 * @author NorthZX
 * @since 2022-06-10
 */
@TableName("et_role_app")
public class EtRoleApp extends DeleteLogicBaseModel {

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
     * 应用ID
     */
    private String appId;

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
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "EtRoleApp{" +
            "id=" + id +
            ", roleId=" + roleId +
            ", appId=" + appId +
        "}";
    }
}
