package com.ragna.knowledge.dto;

import jakarta.validation.constraints.NotBlank;

public record KbCreateReq (
        @NotBlank(message = "知识库名称不能为空") String name,
        String description,
        Integer chunkSize,
        Integer chunkOverlap
){}
