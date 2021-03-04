package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.SysUserRole;

import java.util.List;

/**
 * <p>
 * 用户角色关联 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户Id获取对应的角色Id
     *
     * @param userId
     * @return
     */
    List<SysUserRole> getRoleByUserId(String userId);

    /**
     * 保存用户和角色的关联信息
     *
     * @param userId
     * @param roleIds
     * @return
     */
    boolean addUserRole(String userId, List<String> roleIds);
}
