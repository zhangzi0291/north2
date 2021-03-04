package com.north.sys.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.BaseController;
import com.north.base.Sort;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.sys.dto.SelectFieldDto;
import com.north.sys.entity.SysDict;
import com.north.sys.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
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

        qwlambda.orderByAsc(SysDict::getDictName,SysDict::getDictKey);
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
}
