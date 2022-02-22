package com.north.base.configuration;

import cloud.tianai.captcha.slider.SliderCaptchaApplication;
import cloud.tianai.captcha.template.slider.ResourceStore;
import cloud.tianai.captcha.template.slider.SliderCaptchaResourceManager;
import cloud.tianai.captcha.template.slider.provider.ClassPathResourceProvider;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @author Northzx
 * @version 1.0
 * @since 2022-02-07
 */
@Component
public class ApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    private SliderCaptchaApplication sliderCaptchaApplication;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        SliderCaptchaResourceManager sliderCaptchaResourceManager = sliderCaptchaApplication.getSliderCaptchaResourceManager();
        ResourceStore resourceStore = sliderCaptchaResourceManager.getResourceStore();
        // 清除内置的背景图片
        resourceStore.clearResources();
        // 添加自定义背景图片
        resourceStore.addResource(new cloud.tianai.captcha.template.slider.Resource(ClassPathResourceProvider.NAME, "images/bg1.jpg"));
        resourceStore.addResource(new cloud.tianai.captcha.template.slider.Resource(ClassPathResourceProvider.NAME, "images/bg2.jpg"));
    }
}
