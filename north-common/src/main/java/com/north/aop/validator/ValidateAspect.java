package com.north.aop.validator;

import com.alibaba.fastjson.JSONObject;
import com.north.base.exception.impl.ValidatorExceptionEnum;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-30
 */
@Aspect
@Component
public class ValidateAspect {

    @Pointcut(value = "@annotation(com.north.aop.validator.ValidateParams)")
    public void validateParams() {
    }

    @Pointcut(value = "@annotation(com.north.aop.validator.ValidateParam)")
    public void validateParam() {
    }

    @Before("validateParams()")
    public void doBeforeValidateParams(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        ValidateParams validateParams = AnnotationUtils.getAnnotation(method, ValidateParams.class);
        for (ValidateParam validateParam : validateParams.value()) {
            validate(parameterNames, arguments, validateParam);
        }
    }

    @Before("validateParam()")
    public void doBeforeValidateParam(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        Object[] arguments = joinPoint.getArgs();

        ValidateParam validateParam = AnnotationUtils.getAnnotation(method, ValidateParam.class);
        validate(parameterNames, arguments, validateParam);
    }

    /**
     * 检查参数并抛出异常
     *
     * @param parameterNames
     * @param arguments
     * @param validateParam
     */
    private void validate(String[] parameterNames, Object[] arguments, ValidateParam validateParam) {
        String message = doCheck(validateParam, parameterNames, arguments);
        ValidatorExceptionEnum.VALIDATOR_ERROR.assertFalse(message == null,message);
    }

    /**
     * 检查参数是否通过校验
     *
     * @param validateParam
     * @param parameterNames
     * @param arguments
     * @return
     */
    private String doCheck(ValidateParam validateParam, String[] parameterNames, Object[] arguments) {
        if (checkParamExite(parameterNames, validateParam.parameterName())) {
            Object value = getParamValue(arguments, parameterNames, validateParam.parameterName());

            Boolean flag = validateParam.value().fun.apply(value, validateParam.express());
            if (!flag) {
                String paramName = validateParam.parameterName().contains(".") ? validateParam.parameterName().split("\\.")[1] : validateParam.parameterName();
                String validateMessage = paramName + validateParam.value().msg;
                if (StringUtils.hasLength(validateParam.message())) {
                    validateMessage = validateParam.message();
                }
                return validateMessage;
            }
        }
        return null;
    }

    /**
     * 检查是否存在待校验参数
     *
     * @param paramName
     * @param argName
     * @return
     */
    private Boolean checkParamExite(String[] paramName, String argName) {
        Object value = null;
        String name = argName;
        if (argName.contains(".")) {
            name = argName.split("\\.")[0];
        }
        int index = 0;
        for (String string : paramName) {
            if (string.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取参数的值
     *
     * @param arguments
     * @param paramName
     * @param argName
     * @return
     */
    private Object getParamValue(Object[] arguments, String[] paramName, String argName) {
        String name = argName;
        if (argName.contains(".")) {
            name = argName.split("\\.")[0];
        }
        int index = 0;
        for (String string : paramName) {
            if (string.equals(name)) {
                Object value = arguments[index];
                Boolean hasChild = argName.contains(".");
                argName = hasChild ? argName.split("\\.")[1] : argName;
                if (hasChild) {
                    JSONObject jo = (JSONObject) JSONObject.toJSON(value);
                    return jo.get(argName);
                } else {
                    return value;
                }

            }
            index++;
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("sysuser".contains("."));
    }
}
