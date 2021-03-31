package com.north.base.exception;

import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.north.base.api.ApiErrorCode;
import com.north.base.api.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 统一异常处理
 *
 * @Author North_zx
 * @Date 2020/12/25
 */
@ControllerAdvice
public class AdminGlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(AdminGlobalExceptionHandler.class);

    public static final String DEFALT_ERROR_MSG = "未知错误";

    @ResponseBody
    @ExceptionHandler(value = LoginFailedException.class)
    public R loginFailedExceptionHandler(HttpServletRequest request, HttpServletResponse response, LoginFailedException e) {
        logger.error("Exception:", e);
        String failedMsg = e.getMessage();
        return R.failed(failedMsg);
    }

    @ResponseBody
    @ExceptionHandler(value = NotPermissionException.class)
    public R notPermissionExceptionHandler(HttpServletRequest request, HttpServletResponse response, NotPermissionException e) {
        response.setStatus((int) ApiErrorCode.FORBIDDEN.getCode());
        String message = "无权限访问";
        if (StringUtils.hasLength(e.getMessage())) {
            message = e.getMessage();
        }
        return R.failed(ApiErrorCode.FORBIDDEN, message);
    }

    @ResponseBody
    @ExceptionHandler(value = NotLoginException.class)
    public R saTokenNotLoginExceptionHandler(HttpServletRequest request, HttpServletResponse response, NotLoginException e) {
        response.setStatus((int) ApiErrorCode.UNAUTHORIZED.getCode());
        String message = "当前会话未登录";
        if (e.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未提供token";
        } else if (e.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "token无效";
        } else if (e.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            message = "token已过期";
        } else if (e.getType().equals(NotLoginException.BE_REPLACED)) {
            message = "token已被顶下线";
        } else if (e.getType().equals(NotLoginException.KICK_OUT)) {
            message = "token已被踢下线";
        }
        return R.failed(ApiErrorCode.UNAUTHORIZED, message);
    }

    @ResponseBody
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public R dataIntegrityViolationHandler(HttpServletRequest request, HttpServletResponse response, DataIntegrityViolationException e) {
        logger.error("Database Exception:", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return R.failed("数据库错误");
    }

    @ResponseBody
    @ExceptionHandler(value = NorthImportException.class)
    public R businessExceptionHandler(HttpServletRequest request, HttpServletResponse response, NorthImportException e) throws IOException {
        logger.error("Exception:", e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ExcelImportResult<Map<String, Object>> result = e.getImportResult();
        if (result == null) {
            return R.failed(e.getMessage());
        }
        response.setStatus((int)ApiErrorCode.ImportFieldError.getCode());
        result.getFailWorkbook().write(response.getOutputStream());
        return null;
    }
}
