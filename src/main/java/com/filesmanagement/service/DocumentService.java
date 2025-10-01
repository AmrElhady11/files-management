package com.filesmanagement.service;

import com.filesmanagement.entity.Document;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface DocumentService {
    public void uploadDocument(MultipartFile file);
    public List<String> getDocumentUrl(int userId);
}
