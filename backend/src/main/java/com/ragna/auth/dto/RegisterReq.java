package com.ragna.auth.dto;

import jakarta.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record RegisterReq (
        @NotBlank(message = "企业名称不能为空") String tenantName,
        @NotBlank(message = "用户名不能为空") @Size(min = 3,max = 20) String username,
        @NotBlank(message = "密码不能为空") @Size(min = 6,max = 30) String password
)
{}
