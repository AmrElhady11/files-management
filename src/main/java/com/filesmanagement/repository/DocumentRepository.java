package com.filesmanagement.repository;

import com.filesmanagement.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
    List<Document> findByUserId(int userId);
}
