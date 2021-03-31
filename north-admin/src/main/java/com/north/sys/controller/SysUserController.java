package com.north.sys.controller;


import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.north.aop.permissions.NorthWithoutLogin;
import com.north.base.BaseController;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.base.exception.curd.InsertFailedException;
import com.north.base.exception.curd.UpdateFailedException;
import com.north.excel.verify.SysUserExcelVerifyHandler;
import com.north.redis.cache.SysUserRedisCacheService;
import com.north.sys.dto.UploadDto;
import com.north.sys.entity.SysRole;
import com.north.sys.entity.SysUser;
import com.north.sys.entity.SysUserRole;
import com.north.sys.service.ISysRoleService;
import com.north.sys.service.ISysUserRoleService;
import com.north.sys.service.ISysUserService;
import com.north.util.ExcelUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RedissonClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author NorthZX
 * @since 2020-12-29
 */
@Tag(name = "sys-user-controller 用户")
@RestController
@RequestMapping("/sysUser")
public class SysUserController extends BaseController<SysUser, ISysUserService> {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private ISysRoleService sysRoleService;
    @Resource
    private ISysUserRoleService sysUserRoleService;
    @Resource
    private SysFileController sysFileController;

    @Override
    protected QueryWrapper<SysUser> setListWrapper(SysUser bean, Map<String, String> map) {
        QueryWrapper<SysUser> qw = super.setListWrapper(bean, map);
        LambdaQueryWrapper<SysUser> qwlambda = qw.lambda();

        qwlambda.orderByDesc(SysUser::getCreatedTime);
        if (StringUtils.hasLength(bean.getNickname())) {
            qwlambda.like(SysUser::getNickname, bean.getNickname());
        }
        return qw;
    }

    /**
     * 添加用户，包含用户的角色和头像
     *
     * @param request
     * @param bean
     * @param roleIds
     * @param files
     * @return
     */
    @Transactional
    @Operation(summary = "添加用户", description = "包含用户的角色和头像")
    @RequestMapping(path = "addWithRole", method = RequestMethod.POST)
    public R add(HttpServletRequest request, SysUser bean, @RequestParam(value = "roleIds", required = false) List<String> roleIds
            , @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        //先保存头像
        if (!CollectionUtils.isEmpty(files)) {
            try {
                UploadDto dto = saveFile(request, files.get(0));
                bean.setIconUrl(dto.getDownloadUrl());
            } catch (IOException e) {
                throw new InsertFailedException("保存图片失败");
            }
        }
        ;
        //两次MD5编码
        bean.setPassword(MD5.create().digestHex(bean.getPassword(), StandardCharsets.UTF_8));
        bean.setPassword(MD5.create().digestHex(bean.getPassword(), StandardCharsets.UTF_8));
        //保存用户信息
        R r = super.addJson(bean);
        if (ApiErrorCode.SUCCESS.getCode() != r.getCode()) {
            return r;
        }
        //保存用户和角色的关联信息
        sysUserRoleService.addUserRole(bean.getId(), roleIds);
        return R.ok();
    }

    /**
     * 编辑用户，包含用户的角色和头像
     *
     * @param request
     * @param bean
     * @param roleIds
     * @param files
     * @return
     */
    @Transactional
    @Operation(summary = "编辑用户", description = "包含用户的角色和头像")
    @RequestMapping(path = "editWithRole", method = RequestMethod.POST)
    public R edit(HttpServletRequest request, SysUser bean, @RequestParam(value = "roleIds", required = false) List<String> roleIds
            , @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        //先保存头像
        if (!CollectionUtils.isEmpty(files)) {
            try {
                UploadDto dto = saveFile(request, files.get(0));
                bean.setIconUrl(dto.getDownloadUrl());
            } catch (IOException e) {
                throw new UpdateFailedException("保存图片失败");
            }
        }
        //编辑用户信息
        R r = super.editJson(bean);
        if (ApiErrorCode.SUCCESS.getCode() != r.getCode()) {
            return r;
        }
        //密码用changePassword接口修改
        bean.setPassword(null);
        //删除旧的用户和角色的关联信息重新保存
        if (roleIds != null) {
            LambdaQueryWrapper<SysUserRole> qw = new QueryWrapper().lambda();
            qw.in(SysUserRole::getUserId, bean.getId());
            sysUserRoleService.remove(qw);
            if (!CollectionUtils.isEmpty(roleIds)) {
                sysUserRoleService.addUserRole(bean.getId(), roleIds);
            }
        }
        return R.ok();
    }

    @Transactional
    @Override
    public R delJson(@RequestParam("ids") List<String> ids) {
        if (ids.contains("1")) {
            return R.failed("不能删除admin用户");
        }
        //删除用户和角色的关联信息
        LambdaQueryWrapper<SysUserRole> qw = new QueryWrapper().lambda();
        qw.in(SysUserRole::getUserId, ids);
        sysUserRoleService.remove(qw);

        for (String id : ids) {
            SysUser user = new SysUser();
            user.setId(id);
        }
        //删除用户
        return super.delJson(ids);
    }

    /**
     * 根据用户Id获取对应的角色Id
     *
     * @param userId
     * @return
     */
    @Operation(summary = "获取用户的角色", description = "根据用户Id获取对应的角色Id")
    @RequestMapping(path = "getRoleByUserId", method = RequestMethod.GET)
    public R<List<SysUserRole>> getRoleByUserId(String userId) {
        List<SysUserRole> list = sysUserRoleService.getRoleByUserId(userId);
        return R.ok(list);
    }

    /**
     * 检查用户ID对应的密码是否正确
     *
     * @param userId
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */
    @Operation(summary = "检查密码", description = "检查用户ID对应的密码是否正确")
    @RequestMapping(path = "checkPassword", method = RequestMethod.GET)
    public R checkPassword(String userId, String password) {
        if (!StringUtils.hasLength(userId)) {
            userId = StpUtil.getLoginIdAsString();
        }
        if (!sysUserService.checkPassword(userId, password)) {
            return R.failed(ApiErrorCode.CheckFieldError, "密码错误");
        }
        return R.ok();
    }

    /**
     * 修改用户对应的密码
     *
     * @param userId
     * @param password 密码为明文密码md5编码之后的值
     * @return
     */
    @Operation(summary = "修改密码", description = "修改用户对应的密码")
    @RequestMapping(path = "changePassword", method = RequestMethod.POST)
    public R changePassword(String userId, String password) {
        if (!StringUtils.hasLength(userId)) {
            userId = StpUtil.getLoginIdAsString();
        }
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(password);
        return this.editJson(user);
    }

    /**
     * 重置用户对应的密码
     *
     * @param userId
     * @return
     */
    @Operation(summary = "重置密码", description = "重置用户对应的密码")
    @RequestMapping(path = "resetPassword", method = RequestMethod.POST)
    public R resetPassword(String userId) {
        SysUser user = new SysUser();
        user.setId(userId);
        String password = "888888";
        password = MD5.create().digestHex(password, StandardCharsets.UTF_8);
        password = MD5.create().digestHex(password, StandardCharsets.UTF_8);
        user.setPassword(password);
        return this.editJson(user);
    }

    /**
     * 调用saveFile接口保存上传的文件
     *
     * @param request
     * @param file
     * @return
     * @throws IOException
     */
    private UploadDto saveFile(HttpServletRequest request, MultipartFile file) throws IOException {
        R<UploadDto> r = sysFileController.upload(request, file);
        return r.getData();
    }

    /**
     * 检查登录名是否重复
     *
     * @param checkValue
     * @param originalValue
     * @return
     */
    @Operation(summary = "检查登录名是否重复", description = "检查登录名是否重复")
    @RequestMapping(path = "checkUsername", method = RequestMethod.GET)
    public R checkUsername(String checkValue, String originalValue) {
        if (checkValue.equals(originalValue)) {
            return R.ok();
        }
        if (!sysUserService.checkUsername(checkValue)) {
            return R.failed(ApiErrorCode.CheckFieldError, "角色名称不可重复");
        }
        return R.ok();
    }

    /**
     * 检查用户名是否重复
     *
     * @param checkValue
     * @param originalValue
     * @return
     */
    @Operation(summary = "检查用户名是否重复", description = "检查用户名是否重复")
    @RequestMapping(path = "checkNickname", method = RequestMethod.GET)
    public R checkNickname(String checkValue, String originalValue) {
        if (checkValue.equals(originalValue)) {
            return R.ok();
        }
        if (!sysUserService.checkUsername(checkValue)) {
            return R.failed(ApiErrorCode.CheckFieldError, "角色名称不可重复");
        }
        return R.ok();
    }

    @RequestMapping("onlineUserList")
    public R getOnlineUserList(Page page) {
        Page<SysUser> pageList = new Page<>(page.getCurrent(), page.getSize());
        Long total = sysUserService.getTotalOnlineNum();
        List<String> sessionIds = StpUtil.searchSessionId("", (int) ((page.getCurrent() - 1) * page.getSize()), (int) page.getSize());
        if (sessionIds.size() == 0) {
            return R.ok(page);
        }
        List<String> ids = new ArrayList<>();
        for (String sessionId : sessionIds) {
            ids.add(sessionId.replace("satoken:login:session:", ""));
        }
        List<SysUser> list = sysUserService.listByIds(ids);
        pageList.setRecords(list);
        pageList.setTotal(total);
        return R.ok(pageList);
    }

    @RequestMapping("kickUser")
    public R kickUser(String id) {
        StpUtil.logoutByLoginId(id);
        return R.ok();
    }

    @RequestMapping("import")
    public R importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        ImportParams importParams = new ImportParams();

        List<Map<String, Object>> list = ExcelUtil.importExcel(file.getInputStream(), new SysUserExcelVerifyHandler(new SysUserRedisCacheService(redissonClient, sysUserService)));
        List<Map<String, Object>> importList = new ArrayList<>();
        for (Map<String, Object> map : list) {
            SysUser sysUser = new SysUser();
            sysUser.setUsername(ExcelUtil.getMapStringValue(map, "用户名"));
            sysUser.setPassword(MD5.create().digestHex(MD5.create().digestHex("888888", StandardCharsets.UTF_8)));
            sysUser.setNickname(ExcelUtil.getMapStringValue(map, "昵称"));
            sysUser.setPhone(ExcelUtil.getMapStringValue(map, "手机号"));
            sysUser.setEmail(ExcelUtil.getMapStringValue(map, "EMail"));
            sysUser.setExpiredTime(ExcelUtil.getMapDateValue(map, "过期时间"));
            sysUser.setDescribe(ExcelUtil.getMapStringValue(map, "描述"));

            List<SysRole> sysRoleList = new ArrayList<>();
            String roles = ExcelUtil.getMapStringValue(map, "角色");
            for (String roleNmae : roles.split(",")) {
                QueryWrapper<SysRole> qw = new QueryWrapper<>();
                qw.lambda().eq(SysRole::getRoleName, roleNmae);
                SysRole role = sysRoleService.getOne(qw, false);
                sysRoleList.add(role);
            }

            Map<String, Object> importMap = new HashMap<>();
            importMap.put("user", sysUser);
            importMap.put("roleList", sysRoleList);

            importList.add(importMap);
        }
        sysUserService.importSysUserList(importList);

        return R.ok();
    }

}
