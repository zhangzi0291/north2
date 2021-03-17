package com.north.base.configuration;

import com.north.base.filter.CorsFilter;
import com.north.utils.SpringUtil;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Set;

@Configuration
public class WebConfiguration {

    /**
     * 添加转换器
     *
     * @return
     */
    @Bean
    public void initEditableValidation() {
        Reflections reflections = new Reflections("com.north.base.converter");
        Set<Class<? extends Converter>> set = reflections.getSubTypesOf(Converter.class);
        for (Class<? extends Converter> clazz : set) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) ((ConfigurableApplicationContext) SpringUtil.getApplicationContext()).getBeanFactory();
            beanFactory.registerBeanDefinition(clazz.getSimpleName(), beanDefinition);
        }
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> testFilterRegistration() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        // 添加过滤规则
        registration.addUrlPatterns("/*");
        // 忽略过滤格式
        registration.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*,");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("corsFilter");
        registration.setOrder(1);
        return registration;
    }
}
