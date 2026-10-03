package com.ragna.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ragna.jwt")
public record JwtProperties (
        String secret,
        Integer expireHours
){}
