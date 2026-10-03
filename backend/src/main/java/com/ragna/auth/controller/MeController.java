package com.ragna.auth.controller;

import com.ragna.common.Result;
import com.ragna.tenant.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MeController {

    @GetMapping("/me")
    public Result<Object> me() {
        return Result.ok(TenantContext.get());
    }
}