package com.north.el.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.base.api.R;
import com.north.el.entity.EtAppInfo;
import com.north.el.entity.EtRole;
import com.north.el.entity.EtUserRole;
import com.north.el.service.IEtRoleService;
import com.north.el.service.IEtUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.north.base.BaseController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2022-05-29
 */
@RestController
@RequestMapping("/etRole")
public class EtRoleController extends BaseController<EtRole, IEtRoleService> {

    @Resource
    private IEtUserRoleService etUserRoleService;

    @Operation(summary = "获取角色", description = "")
    @RequestMapping(path = "getRole", method = RequestMethod.GET)
    public R getRole(String userId) {
        QueryWrapper<EtUserRole> qw = Wrappers.query();
        qw.lambda().eq(EtUserRole::getUserId,userId);
        List<EtUserRole> list = etUserRoleService.list(qw);
        List<String> resultList = new ArrayList<>();
        for (EtUserRole etUserRole : list) {
            resultList.add(etUserRole.getRoleId());
        }
        return R.ok(resultList);
    }

    @Transactional
    @Operation(summary = "分配角色", description = "")
    @RequestMapping(path = "distRole", method = RequestMethod.POST)
    public R add(String userId,@RequestParam(value = "roleIds", required = false) List<String> roleIds) {
        QueryWrapper<EtUserRole> qw = Wrappers.query();
        qw.lambda().eq(EtUserRole::getUserId,userId);
        etUserRoleService.remove(qw);

        List<EtUserRole> saveList = new ArrayList<>();
        for (String roleId : roleIds) {
            EtUserRole ur = new EtUserRole();
            ur.setRoleId(roleId);
            ur.setUserId(userId);
            saveList.add(ur);
        }
        etUserRoleService.saveBatch(saveList);

        return R.ok();
    }

}
