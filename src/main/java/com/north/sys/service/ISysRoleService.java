package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 角色 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface ISysRoleService extends IService<SysRole> {

    void assignPermissions(String roleId, List<String> resourceIds);

    /**
     * 通过url获取角色
     *
     * @param url
     * @return
     */
    List<SysRole> getSysRoleByUrl(String url);

    /**
     * 检查角色名称是否重复，不重复返回true，重复返回false
     *
     * @param roleName
     * @return
     */
    Boolean checkRoleName(String roleName);

}
