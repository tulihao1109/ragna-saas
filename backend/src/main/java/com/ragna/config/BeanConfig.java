package com.ragna.config;

import com.ragna.auth.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public JwtUtil jwtUtil(JwtProperties props) {
        return new JwtUtil(props);
    }
}