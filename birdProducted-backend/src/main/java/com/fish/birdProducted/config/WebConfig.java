package com.fish.birdProducted.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fish.birdProducted.interceptor.AccessLimitInterceptor;

/**
 * @author fish
 * <p>
 * 创建时间：2024/2/16 19:56
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AccessLimitInterceptor accessLimitInterceptor;


    /**
     * @param registry
     * 注册拦截器，写好的拦截器需添加注册才能生效
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // redis限流拦截器 （指定拦截路径，往往使用 "/**"）
        registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");
    }
}
