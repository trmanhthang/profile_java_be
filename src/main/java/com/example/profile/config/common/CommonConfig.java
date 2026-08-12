package com.example.profile.config.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;

@Configuration
public class CommonConfig {

    @Bean
    public AntPathMatcher antPathMatcher () {
        return new AntPathMatcher();
    }
}
