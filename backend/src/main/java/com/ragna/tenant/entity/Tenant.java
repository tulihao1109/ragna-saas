package com.ragna.tenant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 租户表 `tenant`
 */
@Data
@TableName("Tenant")
public class Tenant {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;    //企业名称

    private String plan;    // 套餐：FREE / PRO

    private Integer status; // 1 正常 0 禁用

    private String apiKey;  // 开放接口用的 key，rk_ 开头

    private LocalDateTime expireTime;   // 到期时间，null 表示不限

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
