package com.north.sys.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.aop.validator.ValidateParam;
import com.north.aop.validator.ValidateParams;
import com.north.aop.validator.ValidatorEnum;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.sys.entity.SysLog;
import com.north.sys.service.ISysLogService;
import com.north.utils.IpUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 用于记录终端访问记录，由终端记录后上报
     * @param request
     * @param bean
     * @return
     */
    @RequestMapping("saveLog")
    @ValidateParams({
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "bean.moduleName"),
            @ValidateParam(value = ValidatorEnum.NOT_EMPTY, parameterName = "bean.operationName"),
    })
    public R saveLog(HttpServletRequest request,SysLog bean) {

        bean.setUserId(StpUtil.getLoginIdAsString());
        Object nickname = StpUtil.getSession().get("nickname");
        if (nickname != null) {
            bean.setNickname(nickname.toString());
        }
        bean.setLogType(4);
        bean.setIpAddr(IpUtil.getIp(request));
        return R.ok();
    }
}
