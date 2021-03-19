package com.north.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.sys.entity.SysLog;
import com.north.sys.service.ISysLogService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2021-03-09
 */
@RestController
@RequestMapping("/sysLog")
public class SysLogController extends BaseController<SysLog, ISysLogService> {

    @Override
    public R<IPage<SysLog>> listJson(SysLog bean, Page page, Map<String, String> map) {
        return super.listJson(bean, page, map);
    }

    @Override
    protected QueryWrapper<SysLog> setListWrapper(SysLog bean, Map<String, String> map) {
        QueryWrapper<SysLog> qw = super.setListWrapper(bean, map);
        LambdaQueryWrapper<SysLog> qwlambda = qw.lambda();
        qwlambda.orderByDesc(SysLog::getCreatedTime);
        if (StringUtils.hasLength(bean.getModuleName())) {
            qwlambda.like(SysLog::getModuleName, bean.getModuleName());
        }
        if (StringUtils.hasLength(bean.getOperationName())) {
            qwlambda.like(SysLog::getOperationName, bean.getOperationName());
        }
        if (StringUtils.hasLength(bean.getNickname())) {
            qwlambda.like(SysLog::getNickname, bean.getNickname());
        }
        if (bean.getLogType() != null) {
            qwlambda.eq(SysLog::getLogType, bean.getLogType());
        }
        return qw;
    }
}
