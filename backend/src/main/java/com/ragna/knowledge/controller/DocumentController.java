package com.ragna.knowledge.controller;

import com.ragna.common.Result;
import com.ragna.knowledge.entity.KbDocument;
import com.ragna.knowledge.service.IngestService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/kb")
public class DocumentController {

    private final IngestService ingestService;

    public DocumentController(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    @PostMapping("/{kbId}/documents")
    public Result<KbDocument> upload(@PathVariable Long kbId,
                                     @RequestParam("file") MultipartFile file) {
        return Result.ok(ingestService.upload(kbId, file));
    }

    @GetMapping("/{kbId}/documents")
    public Result<List<KbDocument>> list(@PathVariable Long kbId) {
        return Result.ok(ingestService.listDocs(kbId));
    }

    @DeleteMapping("/documents/{docId}")
    public Result<Void> delete(@PathVariable Long docId) {
        ingestService.deleteDocument(docId);
        return Result.ok(null);
    }
}