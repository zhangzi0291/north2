package com.north.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.sys.entity.JsonTableMate;
import com.north.sys.entity.JsonTableValue;
import com.north.sys.mapper.JsonTableValueMapper;
import com.north.sys.service.IJsonTableValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * json数据总表 服务实现类
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@Service
public class JsonTableValueServiceImpl extends ServiceImpl<JsonTableValueMapper, JsonTableValue> implements IJsonTableValueService {

    @Resource
    private JsonTableValueMapper mapper;

    @Override
    public IPage<JSONObject> getTableValue(String tableId, Page page, String search) {
        QueryWrapper<JsonTableValue> qw = Wrappers.query();
        qw.lambda().eq(JsonTableValue::getTableId,tableId);
        if(StringUtils.hasLength(search)){
            qw.lambda().like(JsonTableValue::getJsonValue,search);
        }
        IPage<JSONObject> pageList = this.page(page,qw);
        List<JSONObject> jsonList = new ArrayList<>();
        for (Object object : pageList.getRecords()) {
            JsonTableValue jsonTableValue = (JsonTableValue) object;
            JSONObject jsonObject = JSONObject.parseObject(jsonTableValue.getJsonValue());
            jsonObject.put("id",jsonTableValue.getId());
            jsonObject.put("tableId",jsonTableValue.getTableId());
            jsonList.add(jsonObject);
        }
        pageList.setRecords(jsonList);
        return pageList;
    }

}
