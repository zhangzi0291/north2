package com.north.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.base.exception.impl.CurlExceptionEnum;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 包含增删改查的基础Controller
 *
 * @Author zxn
 * @Date 2018-10-11 12:26
 */
public abstract class BaseController<U extends IBaseModel, T extends IService<U>> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected T service;

    /**
     * 统一的条件查询的入口
     *
     * @param bean
     * @param map
     * @return
     */
    protected QueryWrapper<U> setListWrapper(U bean, Map<String, String> map) {
        QueryWrapper<U> qw = Wrappers.query();
        //前端排序
        Sort sort = BeanUtil.mapToBean(map, Sort.class, false, CopyOptions.create().ignoreError());
        if (sort.isNeedSort()) {
            qw.orderBy(true, sort.isAsc(), com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(sort.getField()));
        }
        return qw;
    }

    /**
     * 分页列表
     *
     * @param bean
     * @param page
     * @param map  自定义扩展字段
     * @return
     */
    @Operation(summary = "获取分页列表", description = "获取分页列表")
    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public R<IPage<U>> listJson(U bean, Page page, @RequestParam Map<String, String> map) {
        QueryWrapper<U> wrapper = setListWrapper(bean, map);
        IPage<U> list = service.page(page, wrapper);
        return R.ok(list);
    }

    /**
     * 获取所有数据
     *
     * @param bean
     * @param map  自定义扩展字段
     * @return
     */
    @Operation(summary = "获取所有数据", description = "获取所有数据")
    @RequestMapping(path = "all", method = {RequestMethod.GET, RequestMethod.POST})
    public R<List<U>> listAllJson(U bean, @RequestParam Map<String, String> map) {
        QueryWrapper<U> wrapper = setListWrapper(bean, map);
        List<U> list = service.list(wrapper);
        return R.ok(list);
    }

    /**
     * 新增
     *
     * @param bean
     * @return
     */
    @Transactional
    @Operation(summary = "新增", description = "新增")
    @RequestMapping(path = "add", method = {RequestMethod.POST})
    public R addJson(U bean) {
        Boolean flag = service.save(bean);

        CurlExceptionEnum.INSERT_FAILED.assertTrue(flag);

        return R.ok();
    }

    /**
     * 编辑
     *
     * @param bean
     * @return
     */
    @Transactional
    @Operation(summary = "编辑", description = "编辑")
    @RequestMapping(path = "edit", method = {RequestMethod.POST})
    public R editJson(U bean) {
        Boolean flag = service.updateById(bean);

        CurlExceptionEnum.UPDATE_FAILED.assertTrue(flag);

        return R.ok();
    }

    /**
     * 通过ID获取单个数据
     *
     * @param id
     * @return
     */
    @Operation(summary = "获取对象", description = "根据id获取对象")
    @RequestMapping(path = "get", method = {RequestMethod.GET})
    public R<U> get(String id) {
        U bean = service.getById(id);
        if (bean == null) {
            return R.failed("无数据");
        }
        return R.ok(bean);
    }

    /**
     * 通过ID批量删除
     *
     * @param ids
     * @return
     */
    @Transactional
    @Operation(summary = "删除", description = "根据id数组删除")
    @RequestMapping(path = "del", method = {RequestMethod.POST})
    public R delJson(@RequestParam("ids") List<String> ids) {
        Boolean flag = service.removeByIds(ids);

        CurlExceptionEnum.DELETE_FAILED.assertTrue(flag);

        return R.ok();
    }

    /**
     * 校验字段是否存在，不存在返回200
     *
     * @param fieldName     pojo类字段名
     * @param checkValue    待校验的值
     * @param originalValue 原始值，如果和待校验值相同相当于没有变化可以通过校验
     * @return
     */
    @Operation(summary = "校验字段重复", description = "校验字段重复")
    @RequestMapping(path = "checkField", method = {RequestMethod.GET})
    public R<U> checkField(String fieldName, String checkValue, String originalValue) {
        if (checkValue.equals(originalValue)) {
            return R.ok();
        }
        List<U> list = service.query().eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName), checkValue).select(com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(fieldName)).list();
        if (list.size() > 0) {
            return R.failed(ApiErrorCode.CheckFieldError, "不可重复");
        }
        return R.ok();
    }

}
