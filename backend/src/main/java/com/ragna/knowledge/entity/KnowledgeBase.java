package com.ragna.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("knowledge_base")
public class KnowledgeBase {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long tenantId;

    private String name;

    private String description;

    private String embeddingModel;   // 记录用的哪个 embedding 模型

    private Integer chunkSize;       // 切分大小（token 数）

    private Integer chunkOverlap;    // 相邻块重叠，保持上下文连贯

    private Integer status;          // 1 正常 0 禁用

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}
