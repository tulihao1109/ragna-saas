package com.ragna;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@MapperScan("com.ragna.**.mapper")
@ConfigurationPropertiesScan("com.ragna.config")
public class RagnaApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagnaApplication.class, args);
    }
}
