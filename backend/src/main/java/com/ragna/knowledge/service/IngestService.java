package com.ragna.knowledge.service;

import com.ragna.common.BizException;
import com.ragna.config.StorageProperties;
import com.ragna.knowledge.entity.KbDocument;
import com.ragna.knowledge.entity.KnowledgeBase;
import com.ragna.knowledge.mapper.KbDocumentMapper;
import com.ragna.knowledge.mapper.KnowledgeBaseMapper;
import com.ragna.tenant.TenantContext;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class IngestService {

    private final KbDocumentMapper docMapper;
    private final KnowledgeBaseMapper kbMapper;
    private final VectorStore vectorStore;
    private final StorageProperties storage;

    public IngestService(KbDocumentMapper docMapper, KnowledgeBaseMapper kbMapper,
                         VectorStore vectorStore, StorageProperties storage) {
        this.docMapper = docMapper;
        this.kbMapper = kbMapper;
        this.vectorStore = vectorStore;
        this.storage = storage;
    }

    @Transactional
    public KbDocument upload(Long kbId, MultipartFile file) {
        KnowledgeBase kb = kbMapper.selectById(kbId);
        if (kb == null) {
            throw new BizException("知识库不存在");
        }
        if (file == null || file.isEmpty()) {
            throw new BizException("文件不能为空");
        }

        LocalDateTime now = LocalDateTime.now();
        String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "unnamed" : file.getOriginalFilename());
        String fileType = extractExt(original);

        // 1) 保存原文件：uploads/{tenantId}/{kbId}/{时间戳-文件名}
        Path targetDir = Paths.get(storage.dir(),
                String.valueOf(TenantContext.tenantId()), String.valueOf(kbId));
        try {
            Files.createDirectories(targetDir);
            Path saved = targetDir.resolve(System.currentTimeMillis() + "-" + original);
            file.transferTo(saved.toFile());

            // 2) 建文档记录（PARSING）
            KbDocument doc = new KbDocument();
            doc.setTenantId(TenantContext.tenantId());
            doc.setKbId(kbId);
            doc.setName(original);
            doc.setFileType(fileType);
            doc.setFileSize(file.getSize());
            doc.setStoragePath(saved.toString());
            doc.setStatus("PARSING");
            doc.setChunkCount(0);
            doc.setCreateTime(now);
            doc.setUpdateTime(now);
            doc.setDeleted(0);
            docMapper.insert(doc);

            // 3) 解析 → 切分 → 向量化
            int chunkCount = doParseAndStore(saved, kb, doc);

            // 4) 更新状态
            doc.setStatus("READY");
            doc.setChunkCount(chunkCount);
            doc.setUpdateTime(LocalDateTime.now());
            docMapper.updateById(doc);
            return doc;

        } catch (Exception e) {
            // 标记失败，便于排查
            throw new BizException("文档导入失败：" + e.getMessage());
        }
    }

    private int doParseAndStore(Path saved, KnowledgeBase kb, KbDocument doc) {
        // Tika 解析
        TikaDocumentReader reader = new TikaDocumentReader(new FileSystemResource(saved));
        List<Document> rawDocs = reader.get();

        // 切分：chunkSize 用知识库配置；minChunkSizeChars 取 350；过滤太短的块
        TokenTextSplitter splitter = new TokenTextSplitter(
                kb.getChunkSize(), 350 , 5 , 10000, true);
        List<Document> chunks = splitter.apply(rawDocs);

        // 补充 metadata（多租户隔离 + 来源追溯，D4 检索要用）
        List<Document> toStore = new ArrayList<>();
        int index = 0;
        for (Document chunk : chunks) {
            Map<String, Object> meta = new HashMap<>(chunk.getMetadata());
            meta.put("tenantId", doc.getTenantId());
            meta.put("kbId", doc.getKbId());
            meta.put("docId", doc.getId());
            meta.put("docName", doc.getName());
            meta.put("chunkIndex", index++);
            toStore.add(new Document(chunk.getText(), meta));
        }

        if (!toStore.isEmpty()) {
            vectorStore.add(toStore);   // 内部批量调 Embedding 并写入 pgvector
        }
        return toStore.size();
    }

    public List<KbDocument> listDocs(Long kbId) {
        return docMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<KbDocument>()
                        .eq(KbDocument::getKbId, kbId)
                        .orderByDesc(KbDocument::getCreateTime));
    }

    @Transactional
    public void deleteDocument(Long docId) {
        KbDocument doc = docMapper.selectById(docId);
        if (doc == null) {
            throw new BizException("文档不存在");
        }
        // 1) 删除向量：按 metadata 里的 docId 过滤
        org.springframework.ai.vectorstore.filter.Filter.Expression expr =
                new org.springframework.ai.vectorstore.filter.Filter.Expression(
                        org.springframework.ai.vectorstore.filter.Filter.ExpressionType.EQ,
                        new org.springframework.ai.vectorstore.filter.Filter.Key("docId"),
                        new org.springframework.ai.vectorstore.filter.Filter.Value(docId));
        vectorStore.delete(expr);

        // 2) 删除文档记录（逻辑删除）
        docMapper.deleteById(docId);
    }

    private String extractExt(String filename) {
        int i = filename.lastIndexOf('.');
        return i >= 0 ? filename.substring(i + 1).toLowerCase() : "unknown";
    }
}