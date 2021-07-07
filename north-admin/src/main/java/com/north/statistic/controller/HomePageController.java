package com.north.statistic.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.base.api.R;
import com.north.sys.service.ISysLogService;
import com.north.sys.service.ISysUserService;
import com.north.util.WebSocketUtil;
import com.north.utils.SpringUtil;
import com.north.utils.SystemUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 总用户数量
     *
     * @return
     */
    @RequestMapping("getTotalUser")
    public R<Long> getTotalUser() {
        return R.ok(sysUserService.count());
    }

    /**
     * 当前在线用户数量
     *
     * @return
     */
    @RequestMapping("getOnlineUser")
    public R<Long> getOnlineUser() {
        return R.ok(sysUserService.getTotalOnlineNum());
    }

    /**
     * 今日登录过的用户数量
     *
     * @return
     */
    @RequestMapping("getTodayUser")
    public R<Long> getTodayUser() {
        return R.ok(sysLogService.getTodayUser());
    }

    @NorthWithoutLogin
    @RequestMapping("notificationTest")
    public R notificationTest() {
        WebSocketUtil.notifyAllUser("通知接口测试", "通知接口内容");
        WebSocketUtil.notifyUser(StpUtil.getLoginIdAsString(), "通知接口测试", "通知接口内容");
        WebSocketUtil.test();
        return R.ok();
    }

    /**
     * 当前服务器硬件监控
     *
     * @return
     */
    @RequestMapping(path = "getHardwareInfo", method = {RequestMethod.GET})
    public R getHardwareInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("cpu", SystemUtil.getSystemCpu());
        result.put("memery", SystemUtil.getSystemMemery());
        result.put("disk", SystemUtil.getSystemDisk());
        return R.ok(result);
    }
}
