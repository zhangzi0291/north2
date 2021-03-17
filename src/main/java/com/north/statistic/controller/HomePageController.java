package com.north.statistic.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.north.base.api.R;
import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserService;
import com.north.utils.WebSocketUtil;
import com.north.websocket.handler.SpringWebSocketHandler;
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

    @RequestMapping("notificationTest")
    public R notificationTest(){
        WebSocketUtil.notifyAllUser("通知接口测试","通知接口内容");
        WebSocketUtil.notifyUser(StpUtil.getLoginIdAsString(),"通知接口测试","通知接口内容");
        WebSocketUtil.notifyUser("2","通知接口测试","通知接口内容");
        return R.ok();
    }
}
