package com.ragna.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ragna.auth.JwtUtil;
import com.ragna.auth.dto.LoginReq;
import com.ragna.auth.dto.LoginResp;
import com.ragna.auth.dto.RegisterReq;
import com.ragna.auth.dto.RegisterResp;
import com.ragna.auth.entity.SysUser;
import com.ragna.auth.mapper.SysUserMapper;
import com.ragna.common.BizException;
import com.ragna.tenant.LoginUser;
import com.ragna.tenant.entity.Tenant;
import com.ragna.tenant.mapper.TenantMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AuthService {

    private final TenantMapper tenantMapper;
    private final SysUserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(TenantMapper tenantMapper, SysUserMapper userMapper, JwtUtil jwtUtil) {
        this.tenantMapper = tenantMapper;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public RegisterResp register(RegisterReq req) {
        Long exists = userMapper.selectCount(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, req.username()));
        if (exists > 0) {
            throw new BizException("用户名已被占用");
        }

        LocalDateTime now = LocalDateTime.now();


        Tenant tenant = new Tenant();
        tenant.setName(req.tenantName());
        tenant.setPlan("FREE");
        tenant.setStatus(1);
        tenant.setApiKey(generateApiKey());
        tenant.setCreateTime(now);
        tenant.setUpdateTime(now);
        tenant.setDeleted(0);
        tenantMapper.insert(tenant);

        SysUser user = new SysUser();
        user.setTenantId(tenant.getId());
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));
        user.setNickname(req.username());
        user.setRole("ADMIN");
        user.setStatus(1);
        user.setCreateTime(now);
        user.setUpdateTime(now);
        user.setDeleted(0);
        userMapper.insert(user);

        return new RegisterResp(tenant.getId(), tenant.getName(), tenant.getApiKey(), user.getUsername());
    }

    public LoginResp login(LoginReq req) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUsername, req.username()));
        if (user == null || !encoder.matches(req.password(), user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BizException("账号已被禁用");
        }
        LoginUser loginUser = new LoginUser(user.getTenantId(), user.getId(), user.getUsername(), "JWT");
        String token = jwtUtil.generate(loginUser);
        return new LoginResp(token, user.getTenantId(), user.getUsername(), user.getNickname(), user.getRole());
    }

    private String generateApiKey() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return "rk_" + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}