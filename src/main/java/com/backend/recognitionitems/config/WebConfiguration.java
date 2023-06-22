package com.backend.recognitionitems.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final String[] inBrowserAllowedMethods =
            new String[]{"HEAD", "GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(inBrowserAllowedMethods);
        //registry.addMapping("/**").allowedOrigins();
    }
}
