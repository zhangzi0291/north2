package com.north.base.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.service.ISysUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private ISysUserService sysUserService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        List<SysResource> roles = sysUserService.getUserResource(loginId.toString());
        List<String> list = roles.stream().map(SysResource::getResourceName).collect(Collectors.toList());
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        List<SysRole> roles = sysUserService.getUserRole(loginId.toString());
        List<String> list = roles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        return list;
    }
}
