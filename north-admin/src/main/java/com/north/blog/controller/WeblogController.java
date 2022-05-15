package com.north.blog.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.base.BaseController;
import com.north.base.api.R;
import com.north.base.converter.LocalDateTimeConverter;
import com.north.base.exception.impl.CurlExceptionEnum;
import com.north.blog.entity.Weblog;
import com.north.blog.service.IWeblogService;
import com.north.sys.controller.SysFileController;
import com.north.sys.dto.UploadDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * <p>
 * 网络日志 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2022-01-21
 */
@RestController
@RequestMapping("/weblog")
public class WeblogController extends BaseController<Weblog, IWeblogService> {

    @Resource
    private SysFileController sysFileController;

    @Override
    protected QueryWrapper<Weblog> setListWrapper(Weblog bean, Map<String, String> map) {
        QueryWrapper<Weblog> qw = super.setListWrapper(bean, map);
        if (StringUtils.hasLength(bean.getWeblogTitle())) {
            qw.lambda().like(Weblog::getWeblogTitle, bean.getWeblogTitle());
        }
        if (StringUtils.hasLength(map.get("startDate"))) {
            LocalDateTimeConverter converter = new LocalDateTimeConverter();
            qw.lambda().ge(Weblog::getCreatedTime, converter.convert(map.get("startDate")));
        }
        if (StringUtils.hasLength(map.get("endDate"))) {
            LocalDateTimeConverter converter = new LocalDateTimeConverter();
            qw.lambda().le(Weblog::getCreatedTime, converter.convert(map.get("endDate")));
        }
        return qw;
    }

    @Transactional
    @Operation(summary = "新增", description = "新增")
    @RequestMapping(path = "addWeblog", method = {RequestMethod.POST})
    public R addJson(HttpServletRequest request, Weblog bean, @RequestParam(value = "files", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                UploadDto dto = sysFileController.saveFile(request, file);
                bean.setWeblogTitleImage(dto.getFileId());
            } catch (IOException e) {
                CurlExceptionEnum.UPDATE_FAILED.assertTrue(false,"保存图片失败");
            }
        }
        String userId = StpUtil.getLoginIdAsString();
        bean.setPublicUserid(userId);
        return super.addJson(bean);
    }

    @Transactional
    @Operation(summary = "编辑", description = "编辑")
    @RequestMapping(path = "editWeblog", method = {RequestMethod.POST})
    public R editJson(HttpServletRequest request, Weblog bean, @RequestParam(value = "files", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                UploadDto dto = sysFileController.saveFile(request, file);
                bean.setWeblogTitleImage(dto.getFileId());
            } catch (IOException e) {
                CurlExceptionEnum.UPDATE_FAILED.assertTrue(false,"保存图片失败");
            }
        }
        String userId = StpUtil.getLoginIdAsString();
        bean.setPublicUserid(userId);
        return super.editJson(bean);
    }

    @NorthWithoutLogin
    @Override
    public R<Weblog> get(String id) {
        R<Weblog> r = super.get(id);
        if (r.getData() != null) {
            r.getData().setCanEdit(StpUtil.getSession().get("userId", "").equals(r.getData().getPublicUserid()));
        }
        return r;
    }

    @NorthWithoutLogin
    @Operation(summary = "获取公开分页列表", description = "获取分页列表")
    @RequestMapping(path = "publicList", method = {RequestMethod.GET, RequestMethod.POST})
    public R<IPage<Weblog>> publicList(Weblog bean, Page page, @RequestParam Map<String, String> map) {
        QueryWrapper<Weblog> wrapper = setListWrapper(bean, map);
        IPage<Weblog> list = service.page(page, wrapper);
        list.getRecords().forEach(d -> {
            if (d.getWeblogText().length() > 55) {
                d.setWeblogText(d.getWeblogText().substring(0, 50) + "\n\n......");
            }
        });
        return R.ok(list);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(1);
        System.out.println(URLEncoder.encode(URLEncoder.encode("+", StandardCharsets.UTF_8)));
        System.out.println(2);
        System.out.println(URLDecoder.decode(URLDecoder.decode("+", StandardCharsets.UTF_8)));
        System.out.println(3);
        System.out.println(URLDecoder.decode("+", StandardCharsets.UTF_8));
    }
}
