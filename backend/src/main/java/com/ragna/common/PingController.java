package com.ragna.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ping")
public class PingController {

    @GetMapping
    public Result<Map<String, Object>> ping() {
        return Result.ok(Map.of("status", "UP", "service", "ragna"));
    }
}