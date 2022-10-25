package com.north.el.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

/**
 * <p>
 * 用户和角色关联
 * </p>
 *
 * @author NorthZX
 * @since 2022-06-10
 */
@TableName("et_user_role")
public class EtUserRole extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "EtUserRole{" +
            "id=" + id +
            ", userId=" + userId +
            ", roleId=" + roleId +
        "}";
    }
}
