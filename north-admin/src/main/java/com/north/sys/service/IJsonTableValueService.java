package com.north.sys.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.north.sys.entity.JsonTableMate;
import com.north.sys.entity.JsonTableValue;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * json数据总表 服务类
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
public interface IJsonTableValueService extends IService<JsonTableValue> {

    IPage<JSONObject> getTableValue(String tableId, Page page,String search);
}
