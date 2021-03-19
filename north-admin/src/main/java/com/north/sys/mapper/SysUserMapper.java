package com.north.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    List<SysRole> getUserRole(String userId);

    List<SysResource> getUserResource(String userId);

}
