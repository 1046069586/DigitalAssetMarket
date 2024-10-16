package com.example.back_end.config;

import com.example.back_end.config.Jwt.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    /**
     * 设置过滤器
     * 此处是配置JWT。JwtInterceptor在Jwt文件夹里
     */

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/users", "/users/login", "/files/**", "/markets");//,
    }

}