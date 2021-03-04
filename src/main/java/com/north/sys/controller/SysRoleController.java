package com.north.sys.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.north.base.BaseController;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.sys.entity.SysResource;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysRoleResource;
import com.north.sys.service.ISysRoleResourceService;
import com.north.sys.service.ISysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Tag(name = "sys-role-controller 角色")
@RestController
@RequestMapping("/sysRole")
public class SysRoleController extends BaseController<SysRole, ISysRoleService> {

    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysRoleResourceService sysRoleResourceService;

    @Override
    protected QueryWrapper<SysRole> setListWrapper(SysRole bean, Map<String, String> map) {
        QueryWrapper<SysRole> qw = super.setListWrapper(bean,map);
        LambdaQueryWrapper<SysRole> qwlambda = qw.lambda();

        qwlambda.orderByDesc(SysRole::getCreatedTime);
        if(StringUtils.hasLength(bean.getRoleName())) {
            qwlambda.like(SysRole::getRoleName, bean.getRoleName());
        }
        return qw;
    }

    /**
     * 添加角色，包含角色所拥有的资源
     *
     * @param bean
     * @param resources
     * @return
     */
    @Transactional
    @Operation(summary = "添加角色", description = "包含角色所拥有的资源")
    @RequestMapping(path = "addWithResource", method = RequestMethod.POST)
    public R addWithResource(SysRole bean, @RequestParam(value = "resources", required = false) List<String> resources) {
        //保存角色
        R r = super.addJson(bean);
        if (ApiErrorCode.SUCCESS.getCode() != r.getCode()) {
            return r;
        }
        //保存角色和资源的关联信息
        sysRoleResourceService.addRoleResouces(bean.getId(), resources);
        return R.ok();
    }

    /**
     * 编辑角色，包含角色所拥有的资源
     *
     * @param bean
     * @param resources
     * @return
     */
    @Transactional
    @Operation(summary = "编辑角色", description = "包含角色所拥有的资源")
    @RequestMapping(path = "editWithResource", method = RequestMethod.POST)
    public R editWithResource(SysRole bean, @RequestParam(value = "resources", required = false) List<String> resources) {
        //保存角色
        R r = super.editJson(bean);
        if (ApiErrorCode.SUCCESS.getCode() != r.getCode()) {
            return r;
        }
        //删除旧的角色和资源的关联信息重新保存
        if (!CollectionUtils.isEmpty(resources)) {
            LambdaQueryWrapper<SysRoleResource> qw = new QueryWrapper().lambda();
            qw.in(SysRoleResource::getRoleId, bean.getId());
            sysRoleResourceService.remove(qw);
            sysRoleResourceService.addRoleResouces(bean.getId(), resources);
        }
        return R.ok();
    }

    /**
     * 检查角色名称重复
     *
     * @param checkValue
     * @param originalValue 旧值，如果有旧值那么旧值和新值可以相同
     * @return
     */
    @Operation(summary = "检查角色名称重复", description = "检查角色名称重复")
    @RequestMapping(path = "checkRoleName", method = RequestMethod.GET)
    public R checkRoleName(String checkValue, String originalValue) {
        if (checkValue.equals(originalValue)) {
            return R.ok();
        }
        if (!sysRoleService.checkRoleName(checkValue)) {
            return R.failed(ApiErrorCode.CheckFieldError, "角色名称不可重复");
        }
        return R.ok();
    }

    @Override
    @Transactional
    public R delJson(@RequestParam("ids") List<String> ids) {
        //删除角色和资源的关联信息
        LambdaQueryWrapper<SysRoleResource> qw = new QueryWrapper().lambda();
        qw.in(SysRoleResource::getRoleId, ids);
        sysRoleResourceService.remove(qw);
        //删除角色
        return super.delJson(ids);
    }

    /**
     * 根据角色Id获取对应的资源Id
     *
     * @param roleId
     * @return
     */
    @Operation(summary = "获取角色的资源", description = "根据角色Id获取对应的资源Id")
    @RequestMapping(path = "getResourceByRoleId", method = RequestMethod.GET)
    public R<List<SysRoleResource>> getResourceByRoleId(String roleId) {
        List<SysRoleResource> list = sysRoleResourceService.getResourceByRoleId(roleId);
        return R.ok(list);
    }
}
