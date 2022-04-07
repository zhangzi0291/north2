package com.north.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysRoleResource;
import com.north.sys.mapper.SysRoleMapper;
import com.north.sys.service.ISysRoleResourceService;
import com.north.sys.service.ISysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Resource
    private ISysRoleResourceService sysRoleResourceService;

    @Override
    public void assignPermissions(String roleId, List<String> resourceIds) {
        LambdaQueryWrapper<SysRoleResource> qw = Wrappers.lambdaQuery();
        qw.eq(SysRoleResource::getRoleId, roleId);
        sysRoleResourceService.remove(qw);

        List<SysRoleResource> rrList = new ArrayList<>();
        for (String resourceId : resourceIds) {
            SysRoleResource sysRoleResource = new SysRoleResource();
            sysRoleResource.setRoleId(roleId);
            sysRoleResource.setResourceId(resourceId);
            rrList.add(sysRoleResource);
        }
        sysRoleResourceService.saveBatch(rrList);
    }

    @Override
    public List<SysRole> getSysRoleByUrl(String url) {
        return baseMapper.getSysRoleByUrl(url);
    }

    @Override
    public Boolean checkRoleName(String roleName) {
        List<SysRole> list = this.lambdaQuery().eq(SysRole::getRoleName, roleName).select(SysRole::getId).list();
        return list.size() <= 0;
    }
}
