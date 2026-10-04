package com.ragna.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * 存储属性
 * @param dir
 */

@ConfigurationProperties(prefix = "ragna.storage")
public record StorageProperties (
        String dir
){}
