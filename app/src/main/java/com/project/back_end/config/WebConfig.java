package com.project.back_end.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; 

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        // Allow CORS for all endpoints
        registry.addMapping("/**")
                .allowedOrigins("*")  // Add your frontend URL here
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Specify allowed methods
                .allowedHeaders("*")  // You can restrict headers if needed
                .allowCredentials(false);  // Set to false when using allowedOrigins("*")
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Ensure static resources are served correctly
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
