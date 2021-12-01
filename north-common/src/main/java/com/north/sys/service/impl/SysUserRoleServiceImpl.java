package com.north.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.north.sys.entity.SysUserRole;
import com.north.sys.mapper.SysUserRoleMapper;
import com.north.sys.service.ISysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 用户角色关联 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Override
    public List<SysUserRole> getRoleByUserId(String userId) {
        LambdaQueryWrapper<SysUserRole> qw = Wrappers.lambdaQuery();
        qw.eq(SysUserRole::getUserId, userId);
        return this.list(qw);
    }

    @Override
    public boolean addUserRole(String userId, List<String> roleIds) {
        List<SysUserRole> urList = new ArrayList<>();
        for (String roleId : roleIds) {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            urList.add(userRole);
        }
        return this.saveBatch(urList);
    }
}
