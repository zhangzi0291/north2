package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysRoleResource;

import java.util.List;

/**
 * <p>
 * 角色菜单关联 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface ISysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 保存角色和资源的关联信息
     *
     * @param roleId
     * @param resourceIds
     * @return
     */
    Boolean addRoleResouces(String roleId, List<String> resourceIds);

    /**
     * 根据角色Id获取对应的资源Id
     *
     * @param roleId
     * @return
     */
    List<SysRoleResource> getResourceByRoleId(String roleId);
}
