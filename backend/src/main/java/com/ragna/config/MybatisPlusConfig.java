package com.ragna.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.ragna.tenant.TenantContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class MybatisPlusConfig {

    /** 这些表不做租户字段拼接 */
    private static final Set<String> IGNORE_TABLES = Set.of("tenant");

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        TenantLineHandler tenantHandler = new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(TenantContext.tenantId());
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                // 没上下文（注册/登录阶段）或显式忽略的表，不拼 tenant_id
                return TenantContext.tenantId() == null
                        || IGNORE_TABLES.contains(tableName.toLowerCase());
            }
        };

        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(tenantHandler));
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
}