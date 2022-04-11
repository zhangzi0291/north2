package com.north.test.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.permissions.NorthWithoutPermissions;
import com.north.base.api.R;
import com.north.utils.SystemUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
@NorthWithoutLogin
@RestController
public class TestController {

    @Resource
    RedisTemplate redisTemplate;

    @Operation(summary = "删除", description = "根据id数组删除")
    @RequestMapping(path = "test/{subscriptionsId}",consumes="application/json")
    public JSONObject test(HttpServletRequest request, HttpServletResponse response, @RequestBody(required = false) String map) {
        System.out.println(map);
        boolean b = StpUtil.isLogin();
        System.out.println(b);
        StpUtil.login("abc");
        b = StpUtil.isLogin();
        System.out.println(b);
        System.out.println(StpUtil.getTokenName());
        b = StpUtil.hasPermission("abc");
        System.out.println(b);
        b = StpUtil.hasPermission("101");
        System.out.println(b);
        JSONObject json = new JSONObject();
        Map<String,String> error = new HashMap<>();
        error.put("errorCode","93102");
        error.put("errorInfo","通知订阅不存在");
        json.put("result","fail");
        json.put("error",error);
        return json;
    }

    @NorthWithoutPermissions()
    @RequestMapping(path = "test2", method = {RequestMethod.GET})
    public R test2() {
        return R.ok("NorthWithoutPermissions");
    }

    @RequestMapping(path = "test3", method = {RequestMethod.GET})
    public R test3() {
        Map<String, Object> result = new HashMap<>();
        result.put("cpu", SystemUtil.getSystemCpu());
        result.put("memery", SystemUtil.getSystemMemery());
        result.put("disk", SystemUtil.getSystemDisk());

        return R.ok(result);
    }

    @RequestMapping(path = "test4", method = {RequestMethod.GET})
    public R test4() throws Exception{
        System.out.println("start");
        Thread.sleep(1000*10);
        Map<String, Object> result = new HashMap<>();
        result.put("cpu", SystemUtil.getSystemCpu());
        result.put("memery", SystemUtil.getSystemMemery());
        result.put("disk", SystemUtil.getSystemDisk());
        System.out.println(JSON.toJSONString(result));
        System.out.println("end");
        return R.ok(result);
    }

}
