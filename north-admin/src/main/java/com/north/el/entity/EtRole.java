package com.north.el.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.DeleteLogicBaseModel;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author NorthZX
 * @since 2022-05-29
 */
@TableName("et_role")
public class EtRole extends DeleteLogicBaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 描述
     */
    private String roleDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    @Override
    public String toString() {
        return "EtRole{" +
            "id=" + id +
            ", roleName=" + roleName +
            ", roleDesc=" + roleDesc +
        "}";
    }
}
