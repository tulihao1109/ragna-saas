package com.ragna.auth.dto;

public record RegisterResp (
        Long tenantId,
        String tenantName,
        String apiKey,
        String username
)
{}
