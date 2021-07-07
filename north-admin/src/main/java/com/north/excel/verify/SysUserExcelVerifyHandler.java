package com.north.excel.verify;

import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import com.north.base.cache.BaseRedisCacheService;
import com.north.sys.controller.SysRoleController;
import com.north.sys.controller.SysUserController;
import com.north.sys.entity.SysUser;
import com.north.util.ExcelUtil;
import com.north.utils.SpringUtil;

import java.util.Map;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-29
 */
public class SysUserExcelVerifyHandler extends BaseExcelVerifyHandler<SysUser> {


    public SysUserExcelVerifyHandler(BaseRedisCacheService<SysUser> baseRedisCacheService) {
        super(baseRedisCacheService);
    }

    @Override
    public SysUser setBean(Map<String, Object> obj) {
        SysUser bean = new SysUser();
        bean.setUsername(ExcelUtil.getMapStringValue(obj, "用户名"));
        bean.setNickname(ExcelUtil.getMapStringValue(obj, "昵称"));
        bean.setPhone(ExcelUtil.getMapStringValue(obj, "手机号"));
        bean.setEmail(ExcelUtil.getMapStringValue(obj, "EMail"));
        bean.setExpiredTime(ExcelUtil.getMapDateValue(obj, "过期时间"));
        bean.setDescribe(ExcelUtil.getMapStringValue(obj, "描述"));
        return bean;
    }

    @Override
    public String verify(Map<String, Object> obj) {
        SysUserController sysUserController = SpringUtil.getBean(SysUserController.class);
        if (ExcelUtil.getMapStringValue(obj, "用户名") == null) {
            return "用户名不可为空";
        } else {
            R r = sysUserController.checkField("username", ExcelUtil.getMapStringValue(obj, "用户名"), null);
            if (r.getCode() != ApiErrorCode.SUCCESS.getCode()) {
                return "用户名," + r.getMsg();
            }
        }
        if (ExcelUtil.getMapStringValue(obj, "昵称") == null) {
            return "昵称不可为空";
        } else {
            R r = sysUserController.checkField("nickname", ExcelUtil.getMapStringValue(obj, "昵称"), null);
            if (r.getCode() != ApiErrorCode.SUCCESS.getCode()) {
                return "昵称," + r.getMsg();
            }
        }
        if (ExcelUtil.getMapStringValue(obj, "手机号") == null) {
            return "手机号不可为空";
        } else {
            R r = sysUserController.checkField("phone", ExcelUtil.getMapStringValue(obj, "手机号"), null);
            if (r.getCode() != ApiErrorCode.SUCCESS.getCode()) {
                return "手机号," + r.getMsg();
            }
        }
        if (ExcelUtil.getMapStringValue(obj, "EMail") == null) {
            return "EMail不可为空";
        } else {
            R r = sysUserController.checkField("email", ExcelUtil.getMapStringValue(obj, "EMail"), null);
            if (r.getCode() != ApiErrorCode.SUCCESS.getCode()) {
                return "EMail," + r.getMsg();
            }
        }
        if (ExcelUtil.getMapStringValue(obj, "角色") == null) {
            return "角色不可为空";
        } else {
            SysRoleController sysRoleController = SpringUtil.getBean(SysRoleController.class);
            R r = sysRoleController.checkField("role_name", ExcelUtil.getMapStringValue(obj, "角色"), null);
            if (r.getCode() == ApiErrorCode.SUCCESS.getCode()) {
                return "角色不存在";
            }
        }
        return null;
    }


}
