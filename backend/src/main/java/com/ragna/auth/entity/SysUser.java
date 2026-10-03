package com.ragna.auth.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class SysUser {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long tenantId;          // 所属租户

    private String username;        // 登录名（本项目要求全局唯一）

    private String password;        // BCrypt 密文

    private String nickname;

    private String role;            // ADMIN / MEMBER

    private Integer status;         // 1 正常 0 禁用

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
