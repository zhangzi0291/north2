package com.north.aop.validator;

import com.north.base.exception.ValidatorException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

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
        if (message != null) {
            throw new ValidatorException(message);
        }
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
        List<String> parameterNameList = Arrays.asList(parameterNames);
        if (parameterNameList.contains(validateParam.parameterName())) {
            Integer num = parameterNameList.indexOf(validateParam.parameterName());
            Object value = arguments[num];
            Boolean flag = validateParam.value().fun.apply(value, validateParam.express());
            if (!flag) {
                String validateMessage = validateParam.parameterName() + validateParam.value().msg;
                if (StringUtils.hasLength(validateParam.message())) {
                    validateMessage = validateParam.message();
                }
                return validateMessage;
            }
        }
        return null;
    }
}
