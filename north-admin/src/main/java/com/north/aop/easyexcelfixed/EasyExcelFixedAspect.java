package com.north.aop.easyexcelfixed;

import cn.afterturn.easypoi.excel.imports.CellValueService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Northzx
 * @version 1.0
 * @since 2021-03-26
 */
@Aspect
@Component
@Configuration
public class EasyExcelFixedAspect {

    @Bean
    public CellValueService CellValueService() {
        return new CellValueService();
    }

    @Pointcut(value = "execution(* cn.afterturn.easypoi.excel.imports.CellValueService.getValue(..))")
    public void access() {

    }


    @Before("access()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] arguments = joinPoint.getArgs();
        System.out.println(Arrays.toString(arguments));
    }
}
