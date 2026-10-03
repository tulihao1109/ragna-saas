package com.ragna.auth.dto;

public record LoginResp (
        String token,
        Long tenantId,
        String username,
        String password,
        String role
)
{}
