package com.north.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.entity.SysRoleResource;
import com.north.sys.mapper.SysRoleResourceMapper;
import com.north.sys.service.ISysRoleResourceService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单关联 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {

    @Override
    public Boolean addRoleResouces(String roleId, List<String> resourceIds) {
        List<SysRoleResource> list = new ArrayList<>();
        for (String resourceId : resourceIds) {
            SysRoleResource rr = new SysRoleResource();
            rr.setRoleId(roleId);
            rr.setResourceId(resourceId);
            list.add(rr);
        }
        return this.saveBatch(list);
    }

    @Override
    public List<SysRoleResource> getResourceByRoleId(String roleId) {
        LambdaQueryWrapper<SysRoleResource> qw = Wrappers.lambdaQuery();
        qw.eq(SysRoleResource::getRoleId, roleId);

        return this.list(qw);
    }
}
