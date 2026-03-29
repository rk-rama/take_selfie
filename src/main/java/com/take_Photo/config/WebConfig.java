package com.take_Photo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Computer ke 'uploads' folder ko browser ke '/uploads/' URL se connect karo
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}