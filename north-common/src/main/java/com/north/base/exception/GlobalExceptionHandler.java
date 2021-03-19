package com.north.base.exception;

import com.north.base.api.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


}
