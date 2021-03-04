package com.north.base.exception;

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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

/**
 * 统一异常处理
 *
 * @Author North_zx
 * @Date 2020/12/25
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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
    @ExceptionHandler(value = NorthBaseException.class)
    public R northBaseExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error("Exception:", e);
        String failedMsg = DEFALT_ERROR_MSG;
        if (StringUtils.hasLength(e.getMessage())) {
            failedMsg = e.getMessage();
        }

        return R.failed(failedMsg);
    }

    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public R violationExceptionHandler(HttpServletRequest request, HttpServletResponse response, ConstraintViolationException e) {
        logger.error("Exception:", e);
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            System.out.println(constraintViolation.getPropertyPath());
            return R.failed(constraintViolation.getMessage());
        }
        response.setStatus(500);
        return R.failed("字段校验失败异常");
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public R businessExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error("Exception:", e);
        response.setStatus(500);
        if (e instanceof DataIntegrityViolationException) {
            return R.failed("数据库错误");
        }
        return R.failed("服务器异常");
    }

}
