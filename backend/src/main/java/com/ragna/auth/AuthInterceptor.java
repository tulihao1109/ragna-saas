package com.ragna.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.ragna.common.BizException;
import com.ragna.tenant.LoginUser;
import com.ragna.tenant.TenantContext;
import com.ragna.tenant.entity.Tenant;
import com.ragna.tenant.mapper.TenantMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String API_KEY_HEADER = "X-Api-Key";

    private final JwtUtil jwtUtil;
    private final TenantMapper tenantMapper;

    public AuthInterceptor(JwtUtil jwtUtil, TenantMapper tenantMapper) {
        this.jwtUtil = jwtUtil;
        this.tenantMapper = tenantMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            try {
                TenantContext.set(jwtUtil.parse(token));
                return true;
            } catch (Exception e) {
                throw new BizException(401, "登录已失效，请重新登录");
            }
        }
        String apiKey = request.getHeader(API_KEY_HEADER);
        if (StringUtils.hasText(apiKey)) {
            Tenant tenant = tenantMapper.selectOne(new LambdaQueryWrapper<Tenant>()
                    .eq(Tenant::getApiKey, apiKey)
                    .eq(Tenant::getStatus, 1));
            if (tenant == null) {
                throw new BizException(401,"无效的 API Key");
            }
            TenantContext.set(new LoginUser(tenant.getId(),null,null,"API_KEY"));
            return true;
        }
        throw new BizException(401, "请先登录");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        TenantContext.clear();   // 防线程复用串号
    }
}