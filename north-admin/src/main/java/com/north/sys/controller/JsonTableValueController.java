package com.north.sys.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.base.exception.curd.DeleteFailedException;
import com.north.excel.verify.SysUserExcelVerifyHandler;
import com.north.redis.cache.SysUserRedisCacheService;
import com.north.sys.entity.*;
import com.north.sys.service.IJsonTableMateService;
import com.north.sys.service.IJsonTableValueService;
import com.north.utils.ExcelUtil;
import com.north.utils.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * json数据总表 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/jsonTableValue")
public class JsonTableValueController extends BaseController<JsonTableValue, IJsonTableValueService> {

    @Resource
    private IJsonTableMateService jsonTableMateService;

    @Override
    protected QueryWrapper<JsonTableValue> setListWrapper(JsonTableValue bean, Map<String, String> map) {
        QueryWrapper<JsonTableValue> qw = super.setListWrapper(bean, map);
        LambdaQueryWrapper<JsonTableValue> qwLambda = qw.lambda();

        qwLambda.eq(JsonTableValue::getTableId,bean.getTableId());
        return qw;
    }

    @Operation(summary = "获取表数据", description = "获取表数据")
    @RequestMapping(path = "getTableValue", method = {RequestMethod.GET})
    public R getTableValue(String tableId, Page page, String search) {
//        LambdaQueryWrapper<JsonTableMate> qw = Wrappers.lambdaQuery();
//        qw.eq(JsonTableMate::getTableId,tableId);
//        List<JsonTableMate> mateList = jsonTableMateService.list(qw);
        IPage<JSONObject> resultList = service.getTableValue(tableId,page,search);
        return R.ok(resultList);
    }

    @Operation(summary = "获取表数据", description = "获取表数据")
    @RequestMapping(path = "getTableValueById", method = {RequestMethod.GET})
    public R getTableValueById(String id) {
        LambdaQueryWrapper<JsonTableValue> qw = Wrappers.lambdaQuery();
        qw.eq(JsonTableValue::getId,id);
        JsonTableValue tableValue = service.getOne(qw,false);
        JSONObject jsonObject = JSONObject.parseObject(tableValue.getJsonValue());
        jsonObject.put("id",tableValue.getId());
        jsonObject.put("tableId",tableValue.getTableId());
        return R.ok(jsonObject);
    }

    @Operation(summary = "新增数据", description = "新增数据")
    @RequestMapping(path = "addTableValue", method = {RequestMethod.POST})
    public R addTableValue(@RequestParam Map<String, String> map) {
        JsonTableValue jtv = JSONObject.parseObject(JSONObject.toJSONString(map),JsonTableValue.class);
        map.remove("id");
        map.remove("tableId");
        jtv.setJsonValue(JSONObject.toJSONString(map));
        service.saveOrUpdate(jtv);
        return R.ok();
    }

    /**
     * 导入用户
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping("import")
    public R importExcel(String tableId,@RequestParam("file") MultipartFile file) throws IOException {
        List<Map<String, Object>> list = ExcelUtil.importExcel(file.getInputStream());
        List<JsonTableValue> importList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            JsonTableValue jtv = new JsonTableValue();
            jtv.setTableId(tableId);
            jtv.setJsonValue(JSONObject.toJSONString(map));
            importList.add(jtv);
        }
        service.saveBatch(importList);
        return R.ok();
    }

    /**
     * 通过ID批量删除
     *
     * @param tableId
     * @return
     */
    @Transactional
    @Operation(summary = "清空数据", description = "清空数据")
    @RequestMapping(path = "delAll", method = {RequestMethod.POST})
    public R delAll(String tableId) {
        QueryWrapper<JsonTableValue> qw = Wrappers.query();
        qw.lambda().eq(JsonTableValue::getTableId,tableId);
        Boolean flag = service.remove(qw);
        if (!flag) {
            throw new DeleteFailedException();
        }
        return R.ok();
    }
}
