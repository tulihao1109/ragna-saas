package com.ragna.knowledge.controller;

import com.ragna.common.Result;
import com.ragna.knowledge.dto.KbCreateReq;
import com.ragna.knowledge.entity.KnowledgeBase;
import com.ragna.knowledge.service.KnowledgeService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/kb")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }

    @PostMapping
    public Result<KnowledgeBase> create(@Valid @RequestBody KbCreateReq req) {
        return Result.ok(knowledgeService.create(req));
    }

    @GetMapping
    public Result<List<KnowledgeBase>> list() {
        return Result.ok(knowledgeService.list());
    }
}