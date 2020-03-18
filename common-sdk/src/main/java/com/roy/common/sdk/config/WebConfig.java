package com.roy.common.sdk.config;

import com.roy.common.sdk.interceptor.ResponseResultInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author chenlin
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    /**
     * 实现拦截器 要拦截的路径以及不拦截的路径
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(new ResponseResultInterceptor()).addPathPatterns("/**").excludePathPatterns("/loginPage", "/login");
    }
}

