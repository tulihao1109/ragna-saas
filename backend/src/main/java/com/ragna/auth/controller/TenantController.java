package com.ragna.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ragna.auth.entity.SysUser;
import com.ragna.auth.mapper.SysUserMapper;
import com.ragna.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {

    private final SysUserMapper userMapper;

    public TenantController(SysUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public Result<List<SysUser>> users() {
        // 插件自动拼 tenant_id，只会返回当前租户的用户
        List<SysUser> list = userMapper.selectList(new LambdaQueryWrapper<SysUser>()
                .orderByDesc(SysUser::getCreateTime));
        list.forEach(u -> u.setPassword(null));   // 不返回密文
        return Result.ok(list);
    }
}