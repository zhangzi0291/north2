package com.north.sys.controller;


import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.BaseController;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.sys.dto.SelectFieldDto;
import com.north.sys.entity.SysDict;
import com.north.sys.service.ISysDictService;
import com.north.utils.ExcelUtil;
import com.north.utils.LambdaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-02-22
 */
@Tag(name = "sys-dict-controller 字典")
@RestController
@RequestMapping("/sysDict")
public class SysDictController extends BaseController<SysDict, ISysDictService> {

    @Resource
    private ISysDictService sysDictService;

    @Override
    protected QueryWrapper<SysDict> setListWrapper(SysDict bean, Map<String, String> map) {
        QueryWrapper<SysDict> qw = super.setListWrapper(bean, map);
        LambdaQueryWrapper<SysDict> qwlambda = qw.lambda();
        List<SFunction<SysDict,?>> orderList = List.of(SysDict::getDictName, SysDict::getDictKey);
        qwlambda.orderByAsc(orderList);
        if (StringUtils.hasLength(bean.getDictName())) {
            qwlambda.like(SysDict::getDictName, bean.getDictName());
        }

        return qw;
    }

    /**
     * 通过字典名称获取下拉框列表
     *
     * @param dictName
     * @return
     */
    @NorthWithoutLogin  
    @Operation(summary = "获取下拉框列表", description = "通过字典名称获取下拉框列表")
    @RequestMapping(path = "getSelectFieldList", method = RequestMethod.GET)
    public R getSelectFieldList(String dictName) {
        LambdaQueryWrapper<SysDict> qw = Wrappers.lambdaQuery();
        qw.eq(SysDict::getDictName, dictName);
        qw.orderByAsc(SysDict::getDictOrder);
        List<SysDict> list = sysDictService.list(qw);
        List<SelectFieldDto> resultList = new ArrayList<>();
        list.forEach(sysDict -> {
            SelectFieldDto fieldDto = new SelectFieldDto();
            fieldDto.setLable(sysDict.getDictKey());
            fieldDto.setValue(sysDictService.getDictValue(sysDict));
            resultList.add(fieldDto);
        });
        return R.ok(resultList);
    }


    @ValidateParams({
            @ValidateParam(parameterName = "dictName", value = ValidatorEnum.NOT_NULL),
            @ValidateParam(parameterName = "dictKey", value = ValidatorEnum.NOT_NULL),
    })
    @Operation(summary = "检查字典是否存在", description = "检查字典是否存在")
    @RequestMapping(path = "checkDict", method = RequestMethod.GET)
    public R checkDict(String dictName, String dictKey) {
        if (!sysDictService.checkDict(dictName, dictKey)) {
            return R.failed(ApiErrorCode.CheckFieldError, "资源名称不可重复");
        }
        return R.ok();
    }

    @RequestMapping(path = "export", method = RequestMethod.GET)
    public R export(HttpServletResponse response, SysDict bean, @RequestParam Map<String, String> map) {
        QueryWrapper<SysDict> wrapper = setListWrapper(bean, map);
        List<SysDict> list = sysDictService.list(wrapper);

        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        entity.add(new ExcelExportEntity("ID", "id"));
        entity.add(new ExcelExportEntity("字典KEY", "dictKey"));
        entity.add(new ExcelExportEntity("字典名称", "dictName"));
        entity.add(new ExcelExportEntity("字典值", "dictValue"));
        entity.add(new ExcelExportEntity("值类型", "valueType"));
        entity.add(new ExcelExportEntity("排序", "dictOrder"));
        entity.add(new ExcelExportEntity("扩展字段", "dictExt"));

        List<Map<String, String>> exportList = new ArrayList<Map<String, String>>();
        Map<String, Object> exprotMap;
        for (SysDict object : list) {
            exprotMap = new HashMap<>();
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getId), object.getId());
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getDictKey), object.getDictKey());
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getDictName), object.getDictName());
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getDictValue), object.getDictValue());
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getValueType), object.getValueType());
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getDictOrder), object.getDictOrder());
            exprotMap.put(LambdaUtil.getFieldName(SysDict::getDictExt), object.getDictExt());
            exportList.add(map);
        }
        try {
            String fileName = "区县管理";
            String suffix = ExcelUtil.getSuffix(list.size());
            String downloadFileName = fileName + suffix;
            byte[] bytes = ExcelUtil.export(fileName, fileName, entity, list);
            response.setHeader("Content-Disposition", "attachment;fileName=" + java.net.URLEncoder.encode(downloadFileName, "UTF-8"));
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            logger.error("导出异常", e);
        }
        return null;
    }


}
