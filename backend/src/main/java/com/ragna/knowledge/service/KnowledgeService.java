package com.ragna.knowledge.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ragna.knowledge.dto.KbCreateReq;
import com.ragna.knowledge.entity.KnowledgeBase;
import com.ragna.knowledge.mapper.KnowledgeBaseMapper;
import com.ragna.tenant.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KnowledgeService {

    private final KnowledgeBaseMapper knowledgeBaseMapper;

    public KnowledgeService(KnowledgeBaseMapper knowledgeBaseMapper) {
        this.knowledgeBaseMapper = knowledgeBaseMapper;
    }

    public KnowledgeBase create(KbCreateReq req) {
        LocalDateTime now = LocalDateTime.now();
        KnowledgeBase kb = new KnowledgeBase();
        kb.setTenantId(TenantContext.tenantId());
        kb.setName(req.name());
        kb.setDescription(req.description());
        kb.setEmbeddingModel("BAAI/bge-m3");
        kb.setChunkSize(req.chunkSize() == null ? 600 : req.chunkSize());
        kb.setChunkOverlap(req.chunkOverlap() == null ? 80 : req.chunkOverlap());
        kb.setStatus(1);
        kb.setCreateTime(now);
        kb.setUpdateTime(now);
        kb.setDeleted(0);
        knowledgeBaseMapper.insert(kb);
        return kb;
    }

    public List<KnowledgeBase> list() {
        return knowledgeBaseMapper.selectList(new LambdaQueryWrapper<KnowledgeBase>()
                .orderByDesc(KnowledgeBase::getCreateTime));
    }

    public KnowledgeBase getById(Long id) {
        return knowledgeBaseMapper.selectById(id);
    }
}
