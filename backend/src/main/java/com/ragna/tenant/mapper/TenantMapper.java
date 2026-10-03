package com.ragna.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ragna.tenant.entity.Tenant;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}