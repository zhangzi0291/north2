package com.north.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.north.sys.entity.SysLog;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-09
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

    Long getTodayUser();
}
