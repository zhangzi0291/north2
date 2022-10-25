package com.north.el.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.base.api.R;
import com.north.base.exception.impl.CurlExceptionEnum;
import com.north.el.entity.EtAppInfo;
import com.north.el.entity.EtRoleApp;
import com.north.el.entity.EtUserRole;
import com.north.el.service.IEtAppInfoService;
import com.north.el.service.IEtRoleAppService;
import com.north.el.service.IEtUserRoleService;
import com.north.sys.controller.SysFileController;
import com.north.sys.dto.UploadDto;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.north.base.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * App信息 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/etAppInfo")
public class EtAppInfoController extends BaseController<EtAppInfo, IEtAppInfoService> {

    @Resource
    private SysFileController sysFileController;
    @Resource
    private IEtRoleAppService etRoleAppService;
    @Resource
    private IEtUserRoleService etUserRoleService;

    @Operation(summary = "获取角色对应的应用", description = "")
    @RequestMapping(path = "getApp", method = RequestMethod.GET)
    public R getRole(String roleId) {
        QueryWrapper<EtRoleApp> qw = Wrappers.query();
        qw.lambda().eq(EtRoleApp::getRoleId,roleId);
        List<EtRoleApp> list = etRoleAppService.list(qw);
        List<String> resultList = new ArrayList<>();
        for (EtRoleApp etRoleApp : list) {
            resultList.add(etRoleApp.getAppId());
        }
        return R.ok(resultList);
    }

    @Transactional
    @Operation(summary = "分配应用", description = "")
    @RequestMapping(path = "distApp", method = RequestMethod.POST)
    public R add(String roleId,@RequestParam(value = "appIds", required = false) List<String> appIds) {
        QueryWrapper<EtRoleApp> qw = Wrappers.query();
        qw.lambda().eq(EtRoleApp::getRoleId,roleId);
        etRoleAppService.remove(qw);

        List<EtRoleApp> saveList = new ArrayList<>();
        for (String appId : appIds) {
            EtRoleApp ra = new EtRoleApp();
            ra.setRoleId(roleId);
            ra.setAppId(appId);
            saveList.add(ra);
        }
        etRoleAppService.saveBatch(saveList);

        return R.ok();
    }

    @Transactional
    @Operation(summary = "添加用户", description = "包含用户的角色和头像")
    @RequestMapping(path = "addApp", method = RequestMethod.POST)
    public R add(HttpServletRequest request, EtAppInfo bean, @RequestParam(value = "files", required = false) MultipartFile file) {
        //保存图片
        if (file != null) {
            try {
                UploadDto dto = sysFileController.saveFile(request, file);
                bean.setImgId(dto.getFileId());
            } catch (IOException e) {
                CurlExceptionEnum.INSERT_FAILED.assertTrue(false,"保存图片失败");
            }
        }
        return super.addJson(bean);
    }


    @Operation(summary = "获取用户对应的应用", description = "")
    @RequestMapping(path = "getAppByUserId", method = RequestMethod.GET)
    public R getAppByUserId(String userId) {
        if(userId.equals("1")){
            QueryWrapper<EtAppInfo> qw3 = Wrappers.query();
            List<EtAppInfo> resultList = service.list(qw3);
            return R.ok(resultList);
        }
        QueryWrapper<EtUserRole> qw = Wrappers.query();
        qw.lambda().eq(EtUserRole::getUserId,userId);
        List<EtUserRole> list = etUserRoleService.list(qw);
        List<String> roleIdList = new ArrayList<>();
        for (EtUserRole etUserRole : list) {
            roleIdList.add(etUserRole.getRoleId());
        }
        if(CollectionUtils.isEmpty(roleIdList)){
            return R.ok();
        }
        QueryWrapper<EtRoleApp> qw2 = Wrappers.query();
        qw2.lambda().in(EtRoleApp::getRoleId,roleIdList);
        List<EtRoleApp> list2 = etRoleAppService.list(qw2);
        List<String> appIdList = new ArrayList<>();
        for (EtRoleApp etRoleApp : list2) {
            appIdList.add(etRoleApp.getAppId());
        }
        if(CollectionUtils.isEmpty(appIdList)){
            return R.ok();
        }
        QueryWrapper<EtAppInfo> qw3 = Wrappers.query();
        qw3.lambda().in(EtAppInfo::getId,appIdList);
        List<EtAppInfo> resultList = service.list(qw3);
        return R.ok(resultList);
    }
}
