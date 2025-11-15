package com.example.scholarship.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类
 * 
 * @author System
 * @version 1.0.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 配置CORS跨域访问
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源处理
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源映射
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        
        // 上传文件映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        
        // 图片资源映射
        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
        
        // CSS资源映射
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
        
        // JS资源映射
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
    }
}