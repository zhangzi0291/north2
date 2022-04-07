package com.north.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.dto.TreeDto;
import com.north.sys.entity.SysResource;
import com.north.sys.mapper.SysResourceMapper;
import com.north.sys.mapper.SysUserMapper;
import com.north.sys.service.ISysResourceService;
import com.north.utils.BaseUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements ISysResourceService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<TreeDto<SysResource>> getSysResourceTreeList(String userId, List<Integer> resourceType, String pid) {
        List<SysResource> list = sysUserMapper.getUserResource(userId);

        List<TreeDto<SysResource>> treeList = new ArrayList<>();
        for (SysResource resource : list) {
            if (resourceType == null || resourceType.contains(resource.getResourceType())) {
                TreeDto<SysResource> treeDto = new TreeDto<>();
                treeDto.setId(resource.getId());
                treeDto.setPid(resource.getParentId());
                treeDto.setData(resource);
                treeList.add(treeDto);
            }
        }
        return BaseUtil.getTreeDtoList(treeList, pid);
    }

    @Override
    public SysResource getSysResourceByUrl(String url) {
        QueryWrapper qw = new QueryWrapper();
        qw.eq("resource_url", url);
        SysResource resource = this.getOne(qw, false);
        return resource;
    }

    @Override
    public boolean checkPermissions(String url) {

        return false;
    }

    @Override
    public Boolean checkResourceName(String roleName) {
        List<SysResource> list = this.lambdaQuery().eq(SysResource::getResourceName, roleName).select(SysResource::getId).list();
        return list.size() <= 0;
    }
}
