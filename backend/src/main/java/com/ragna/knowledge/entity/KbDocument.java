package com.ragna.knowledge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("kb_document")
public class KbDocument {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long tenantId;

    private Long kbId;

    private String name;

    private String fileType;        // pdf / docx / txt / md

    private Long fileSize;

    private String storagePath;     // 原文件保存路径

    private String fileHash;        // 可用于去重（本课程可不实现）

    private String status;          // PARSING / READY / FAILED

    private Integer chunkCount;

    private String errorMsg;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer deleted;
}