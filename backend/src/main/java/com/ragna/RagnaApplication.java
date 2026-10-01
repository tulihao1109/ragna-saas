package com.ragna;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ragna.**.mapper")
public class RagnaApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagnaApplication.class, args);
    }
}
