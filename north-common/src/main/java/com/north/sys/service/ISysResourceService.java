package com.north.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.dto.TreeDto;
import com.north.sys.entity.SysResource;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface ISysResourceService extends IService<SysResource> {

    /**
     * 获取用户对应的资源
     *
     * @param userId       用户ID
     * @param resourceType 资源类型，null所有，1菜单，2资源
     * @param pid          根目录ID
     * @return
     */
    List<TreeDto<SysResource>> getSysResourceTreeList(String userId, List<Integer> resourceType, String pid);

    SysResource getSysResourceByUrl(String url);

    boolean checkPermissions(String url);

    /**
     * 检查资源名称是否重复，不重复返回true，重复返回false
     *
     * @param roleName
     * @return
     */
    Boolean checkResourceName(String roleName);
}
