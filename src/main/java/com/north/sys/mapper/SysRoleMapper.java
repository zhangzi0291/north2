package com.north.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.north.sys.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> getSysRoleByUrl(String url);
}
