package com.north.redis.cache;

import com.north.base.cache.BaseRedisCacheService;
import com.north.sys.entity.SysUser;
import com.north.sys.service.ISysUserService;
import org.redisson.api.RedissonClient;

import java.util.List;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-30
 */
public class SysUserRedisCacheService extends BaseRedisCacheService<SysUser> {

    public SysUserRedisCacheService(RedissonClient redissonClient, ISysUserService sysUserService) {
        super(redissonClient);
        this.sysUserService = sysUserService;
    }

    private final ISysUserService sysUserService;

    @Override
    protected List<SysUser> getAllList() {
        return sysUserService.list();
    }

    @Override
    public Boolean checkBean(SysUser bean, SysUser listBean) {
        return bean.getUsername().equals(listBean.getUsername());
    }

}
