package com.north.sys.mapper;

import com.north.sys.entity.JsonTableValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
 * <p>
 * json数据总表 Mapper 接口
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
public interface JsonTableValueMapper extends BaseMapper<JsonTableValue> {

    Map<String, Object> getTableValue();

}
