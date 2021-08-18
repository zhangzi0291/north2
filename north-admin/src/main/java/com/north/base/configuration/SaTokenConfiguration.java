package com.north.base.configuration;

import com.north.aop.permissions.NorthCheckPermissionsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Northzx
 * @version 1.0
 * @since 2020-12-25
 */
@Configuration
public class SaTokenConfiguration implements WebMvcConfigurer {

    /**
     * 注册sa-token的拦截器，打开注解式鉴权功能
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NorthCheckPermissionsInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/error")

        ;
    }

}
