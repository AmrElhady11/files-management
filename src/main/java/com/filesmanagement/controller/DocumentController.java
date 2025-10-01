package com.filesmanagement.controller;

import com.filesmanagement.entity.Document;
import com.filesmanagement.response.UrlResponse;
import com.filesmanagement.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/docs")
public class DocumentController {
    private final DocumentService documentService;
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        documentService.uploadDocument(file);
        return ResponseEntity.ok().body("Documents uploaded successfully");
    }

    @GetMapping("/getAllDocument/{userId}")
    public ResponseEntity<List<UrlResponse>> getAllDocument(@PathVariable int userId) {
    List<UrlResponse> responses = new ArrayList<>();
    documentService.getDocumentUrl(userId).forEach(url -> responses.add(new UrlResponse(url)));
    return ResponseEntity.ok(responses);
    }
}
