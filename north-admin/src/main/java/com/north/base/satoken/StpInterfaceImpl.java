package com.north.base.satoken;

import cn.dev33.satoken.stp.StpInterface;
import com.north.base.Constant;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.service.ISysUserService;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ISysUserService sysUserService;

    private final int timeout = 30;

    @Override
    public List<String> getPermissionList(Object loginId, String loginKey) {
        RList<String> rList = redissonClient.getList(Constant.SATOKEN_REDIS_PERMISSION_KEY+loginId);
        if(rList.isExists() && !rList.isEmpty()){
            return rList.range(-1);
        }
        List<SysResource> roles = sysUserService.getUserResource(loginId.toString());
        List<String> list = roles.stream().map(SysResource::getResourceName).collect(Collectors.toList());
        list.forEach(s ->rList.add(s));
        rList.expire(timeout, TimeUnit.MINUTES);
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginKey) {
        RList<String> rList = redissonClient.getList(Constant.SATOKEN_REDIS_ROLE_KEY+loginId);
        if(rList.isExists() && !rList.isEmpty()){
            return rList.range(-1);
        }
        List<SysRole> roles = sysUserService.getUserRole(loginId.toString());
        List<String> list = roles.stream().map(SysRole::getRoleName).collect(Collectors.toList());
        list.forEach(s ->rList.add(s));
        rList.expire(timeout, TimeUnit.MINUTES);
        return list;
    }
}
