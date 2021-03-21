package com.north.test.controller;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.permissions.NorthWithoutPermissions;
import com.north.base.api.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
@RestController
public class TestController {

    @Resource
    RedisTemplate redisTemplate;

    @NorthWithoutLogin
    @Operation(summary = "删除", description = "根据id数组删除")
    @RequestMapping(path = "test", method = {RequestMethod.GET})
    public R test() {
        boolean b = StpUtil.isLogin();
        System.out.println(b);
        StpUtil.setLoginId("abc");
        b = StpUtil.isLogin();
        System.out.println(b);
        System.out.println(StpUtil.getTokenName());
        b=StpUtil.hasPermission("abc");
        System.out.println(b);
        b=StpUtil.hasPermission("101");
        System.out.println(b);
        return R.ok("12312312dsfasfdasfas");
    }

    @NorthWithoutPermissions()
    @RequestMapping(path = "test2", method = {RequestMethod.GET})
    public R test2() {
        return R.ok("NorthWithoutPermissions");
    }

    @RequestMapping(path = "test3", method = {RequestMethod.GET})
    public R test3() {
        System.out.println(StpUtil.getSessionTimeout());
        System.out.println(StpUtil.getTokenTimeout());
        System.out.println(StpUtil.getTokenActivityTimeout());
        return R.ok("checkLogin");
    }
}
