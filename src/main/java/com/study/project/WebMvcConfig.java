package com.study.project;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	
    	String projectPath = System.getProperty("user.dir");
        String uploadPath = "file:" + projectPath + "/uploads/";
        
        System.out.println("설정된 물리 경로: " + uploadPath);
        
        registry.addResourceHandler("/uploads/**")
        .addResourceLocations(uploadPath);
    }
}