package com.north.statistic.controller;

import com.north.base.api.R;
import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-08
 */
@RestController
@RequestMapping("home")
public class HomePageController {

    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysLogService sysLogService;

    @RequestMapping("getTotalUser")
    public R<Long> getTotalUser(){
        return R.ok(sysUserService.count());
    }

    @RequestMapping("getOnlineUser")
    public R<Long> getOnlineUser(){
        return R.ok(sysUserService.getTotalOnlineNum());
    }

    @RequestMapping("getTodayUser")
    public R<Long> getTodayUser(){
        return R.ok(sysLogService.getTodayUser());
    }

}
