package com.north.base;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.north.base.api.R;
import com.north.base.exception.curd.DeleteFailedException;
import com.north.base.exception.curd.UpdateFailedException;
import com.north.sys.entity.SysDict;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
public abstract class BaseController<U extends BaseModel, T extends IService<U>> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private T service;

    /**
     * list和all接口的条件查询的入口
     *
     * @param bean
     * @param map
     * @return
     */
    protected QueryWrapper<U> setListWrapper(U bean, Map<String, String> map) {
        QueryWrapper<U> qw = Wrappers.query();
        //前端排序
        Sort sort = BeanUtil.mapToBean(map,Sort.class,false, CopyOptions.create().ignoreError());
        if(sort.isNeedSort()){
            qw.orderBy(true,sort.isAsc(), com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(sort.getField()));
        }
        return qw;
    }

    @Operation(summary = "获取分页列表", description = "获取分页列表")
    @RequestMapping(path = "list", method = {RequestMethod.GET, RequestMethod.POST})
    public R<IPage<U>> listJson(U bean, Page page, @RequestParam Map<String, String> map) {
        QueryWrapper<U> wrapper = setListWrapper(bean, map);
        IPage<U> list = service.page(page, wrapper);
        return R.ok(list);
    }

    @Operation(summary = "获取所有参数", description = "获取所有参数")
    @RequestMapping(path = "all", method = {RequestMethod.GET, RequestMethod.POST})
    public R<List<U>> listAllJson(U bean, @RequestParam Map<String, String> map) {
        QueryWrapper<U> wrapper = setListWrapper(bean, map);
        List<U> list = service.list(wrapper);
        return R.ok(list);
    }

    @Transactional
    @Operation(summary = "新增", description = "新增")
    @RequestMapping(path = "add", method = {RequestMethod.POST})
    public R addJson(U bean) {
        Boolean flag = service.save(bean);
        if (!flag) {
            return R.failed("添加失败");
        }
        return R.ok();
    }

    @Transactional
    @Operation(summary = "编辑", description = "编辑")
    @RequestMapping(path = "edit", method = {RequestMethod.POST})
    public R editJson(U bean) {
        Boolean flag = service.updateById(bean);
        if (!flag) {
            throw new UpdateFailedException();
        }
        return R.ok();
    }

    @Operation(summary = "获取对象", description = "根据id获取对象")
    @RequestMapping(path = "get", method = {RequestMethod.GET})
    public R<U> get(String id) {
        U bean = service.getById(id);
        if (bean == null) {
            return R.failed("无数据");
        }
        return R.ok(bean);
    }


    @Transactional
    @Operation(summary = "删除", description = "根据id数组删除")
    @RequestMapping(path = "del", method = {RequestMethod.POST})
    public R delJson(@RequestParam("ids") List<String> ids) {
        Boolean flag = service.removeByIds(ids);
        if (!flag) {
            throw new DeleteFailedException();
        }
        return R.ok();
    }

}
