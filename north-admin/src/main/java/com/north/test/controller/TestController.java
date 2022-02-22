package com.north.test.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.aop.permissions.NorthWithoutPermissions;
import com.north.base.api.R;
import com.north.test.controller.test.ApiUtils;
import com.north.test.controller.test.wav;
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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
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

    @RequestMapping(path = "upload")
    public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // TODO Auto-generated method stub
        //response.getWriter().append("Served at: ").append(request.getContextPath());
        request.setCharacterEncoding("UTF-8");

        String blob_base64 = request.getParameter("upfile_b64");

        byte[] bytes_wav = ApiUtils.Convert_Base64ToBytes(blob_base64);
        File file=new File("D:\\workspace-study\\north-2.0\\1.wav");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(bytes_wav);

        wav wavefile = new wav();
        wavefile.GetFromBytes(bytes_wav);
        //response.getWriter().append(new String(bytes_wav, "utf-8"));
        //System.out.println(wavefile.fs);
        //System.out.println(wavefile.channels);
        //System.out.println(wavefile.sample_width);
        //System.out.print(wavefile.samples.length);
        String result = ApiUtils.SendToASRTServer("qwertasd", wavefile.fs, wavefile.samples);
        //System.out.println(result);
        //response.getWriter().append("Served at: ").append(request.getContextPath()).append(blob_base64);
        //response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().append(result);

//        return R.ok(result);
    }

    public static void main(String[] args) {
        wav wavefile = new wav();
        String blob_base64=Base64.encode(new File("C:\\Users\\North\\Desktop\\zhangsan.wav"));
        byte[] bytes_wav = ApiUtils.Convert_Base64ToBytes(blob_base64);
        wavefile.GetFromBytes(bytes_wav);
        String result = ApiUtils.SendToASRTServer("qwertasd", wavefile.fs, wavefile.samples);
        System.out.println(result);
    }
}
