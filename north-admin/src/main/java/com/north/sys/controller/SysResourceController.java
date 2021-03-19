package com.north.sys.controller;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.north.base.BaseController;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.sys.dto.TreeDto;
import com.north.sys.entity.SysResource;
import com.north.sys.service.ISysResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Tag(name = "sys-resource-controller 菜单资源")
@RestController
@RequestMapping("/sysResource")
public class SysResourceController extends BaseController<SysResource, ISysResourceService> {

    @Resource
    private ISysResourceService sysResourceService;

    @Override
    protected QueryWrapper<SysResource> setListWrapper(SysResource bean, Map<String, String> map) {
        QueryWrapper<SysResource> qw = super.setListWrapper(bean,map);
        LambdaQueryWrapper<SysResource> qwlambda = qw.lambda();

        qwlambda.orderByDesc(SysResource::getCreatedTime);

        return qw;
    }

    /**
     * 获取当前登陆用户的所有资源
     *
     * @param pid 根节点ID
     * @return
     */
    @Operation(summary = "获取当前登陆用户的所有资源", description = "获取当前登陆用户的所有资源")
    @RequestMapping(path = "getResourceTree", method = RequestMethod.GET)
    public R getResourceTree(String pid) {
        String userId = StpUtil.getLoginIdAsString();
        List<TreeDto<SysResource>> list;
        if (userId.equals("1")) {
            list = sysResourceService.getSysResourceTreeList(null, null, pid);
        } else {
            list = sysResourceService.getSysResourceTreeList(userId, null, pid);
        }

        return R.ok(list);
    }

    /**
     * 获取当前登陆用户的所有菜单
     *
     * @param pid 根节点ID
     * @return
     */
    @Operation(summary = "获取当前登陆用户的所有菜单", description = "获取当前登陆用户的所有菜单")
    @RequestMapping(path = "getMenuTree", method = RequestMethod.GET)
    public R getMenuTree(String pid) {
        String userId = StpUtil.getLoginIdAsString();
        List<TreeDto<SysResource>> list;
        List<Integer> resourceType = Arrays.asList(new Integer[]{1, 4, 5});
        if (userId.equals("1")) {
            list = sysResourceService.getSysResourceTreeList(null, resourceType, pid);
        } else {
            list = sysResourceService.getSysResourceTreeList(userId, resourceType, pid);
        }
        return R.ok(list);
    }

    /**
     * 检查资源名称重复
     *
     * @param checkValue
     * @param originalValue 旧值，如果有旧值那么旧值和新值可以相同
     * @return
     */
    @Operation(summary = "检查资源名称重复", description = "检查资源名称重复")
    @RequestMapping(path = "checkResourceName", method = RequestMethod.GET)
    public R checkResourceName(String checkValue, String originalValue) {
        if (checkValue.equals(originalValue)) {
            return R.ok();
        }
        if (!sysResourceService.checkResourceName(checkValue)) {
            return R.failed(ApiErrorCode.CheckFieldError, "资源名称不可重复");
        }
        return R.ok();
    }

    public static void main(String[] args) {
        System.out.println("563acb74-5cee-4d89-a9cd-e8e6c538a968".length());
    }
}
