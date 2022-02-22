package com.north.sys.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.base.api.R;
import com.north.sys.entity.JsonTableMate;
import com.north.sys.service.IJsonTableMateService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.north.base.BaseController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * json表结构 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/jsonTableMate")
public class JsonTableMateController extends BaseController<JsonTableMate, IJsonTableMateService> {

    @Transactional
    @Operation(summary = "新增或编辑元数据列表", description = "新增或编辑元数据列表")
    @RequestMapping(path = "addMateList", method = {RequestMethod.POST})
    public R addMateList(@RequestBody JSONObject mateData) {
        String tableId = mateData.getString("tableId");

        QueryWrapper<JsonTableMate> qw = Wrappers.query();
        qw.lambda().eq(JsonTableMate::getTableId,tableId);
        service.remove(qw);

        List<JsonTableMate> mateList= mateData.getJSONArray("mateList").toJavaList(JsonTableMate.class);
        mateList.forEach(mate ->{
            mate.setTableId(tableId);
        });
        service.saveBatch(mateList);
        Boolean flag = true;
        if (!flag) {
            return R.failed("添加失败");
        }
        return R.ok();
    }

    @Operation(summary = "获取表的元数据列表", description = "获取表的元数据列表")
    @RequestMapping(path = "getMateList", method = {RequestMethod.POST})
    public R getMateList(String tableId) {
        LambdaQueryWrapper<JsonTableMate> qw = Wrappers.lambdaQuery();
        qw.eq(JsonTableMate::getTableId,tableId);
        qw.orderByAsc(JsonTableMate::getOrderNum);
        List<JsonTableMate> mateList = service.list(qw);
        return R.ok(mateList);
    }

}
