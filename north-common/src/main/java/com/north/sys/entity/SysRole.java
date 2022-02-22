package com.north.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.north.base.LogicDeleteBaseModel;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@TableName("sys_role")
public class SysRole extends LogicDeleteBaseModel implements Serializable {

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
    private String describe;

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

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SysRole.class.getSimpleName() + "[", "]")
                .add("id='" + id + "'")
                .add("roleName='" + roleName + "'")
                .add("describe='" + describe + "'")
                .toString();
    }
}
