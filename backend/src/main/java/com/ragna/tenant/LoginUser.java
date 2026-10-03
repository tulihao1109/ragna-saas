package com.ragna.tenant;

public record LoginUser(
        Long tenantId,
        Long userId,          // API Key 调用时为 null
        String username,
        String authType       // JWT / API_KEY
){}



