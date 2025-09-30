package com.filesmanagement.service;

import com.filesmanagement.entity.Document;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    public void uploadDocument(MultipartFile file);
    public Document getDocument(int userId);
}
