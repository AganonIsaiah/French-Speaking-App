package com.example.frenchlearningapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * To customize resource handling
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configures resource handlers to serve audio files
     *
     * @param registry For resource handler
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/audio/**").addResourceLocations("classpath:/audio/");
    }
}

