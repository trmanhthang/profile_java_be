package com.example.profile.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApiPrefixConfig implements WebMvcConfigurer {
    @Value("${app.apiPrefix}")
    private String apiPrefix;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(
                this.apiPrefix,
                clazz -> clazz.isAnnotationPresent(RestController.class)
        );
    }
}
