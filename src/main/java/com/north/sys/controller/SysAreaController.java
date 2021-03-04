package com.north.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.north.base.BaseController;
import com.north.sys.entity.SysArea;
import com.north.sys.service.ISysAreaService;
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
 * @since 2021-03-04
 */
@RestController
@RequestMapping("/sysArea")
public class SysAreaController extends BaseController<SysArea, ISysAreaService> {

    @Override
    protected QueryWrapper<SysArea> setListWrapper(SysArea bean, Map<String, String> map) {
        QueryWrapper<SysArea> qw = super.setListWrapper(bean, map);
        LambdaQueryWrapper<SysArea> qwlambda = qw.lambda();
        if (StringUtils.hasLength(bean.getAreaName())) {
            qwlambda.like(SysArea::getAreaName, bean.getAreaName());
        }
        if (bean.getAreaLevel() != null) {
            qwlambda.eq(SysArea::getAreaLevel, bean.getAreaLevel());
        }
        if (bean.getParentId() != null) {
            qwlambda.eq(SysArea::getParentId, bean.getParentId());
        }
        return qw;
    }
}
