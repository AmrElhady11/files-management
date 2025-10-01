package com.filesmanagement.service.impl;

import com.filesmanagement.entity.Document;
import com.filesmanagement.repository.DocumentRepository;
import com.filesmanagement.service.DocumentService;
import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final MinioClient minioClient;
    @Transactional
    @Override
    public void uploadDocument(MultipartFile file) {
        String bucketName = getBucketName(file.getContentType());
        String objectName = String.format("users/user%d/%s",getUser(), file.getOriginalFilename());
        saveDocumentToBucket(file, bucketName, objectName);
        saveDocumentToDB(file, bucketName, objectName);
    }


    @Override
    public List<String> getDocumentUrl(int userId) {
        List<Document> documents = documentRepository.findByUserId(userId);
        List<String> documentUrls = new ArrayList<>();
        documents.forEach(document -> {documentUrls.add(getDocumentUrlFromBucket(document.getBucketName(),document.getObjectKey()));}
        );

        return documentUrls;
    }

    private String getBucketName(String contentType){
        if(contentType.startsWith("image")){
            return "images-bucket";
        }
        if(contentType.startsWith("video")){
            return "videos-bucket";
        }
        if(contentType.startsWith("audio")){
            return "audios-bucket";
        }
        if(contentType.startsWith("text")){
            return "text-bucket";
        }
        if(contentType.startsWith("application")){
            return "pdf-bucket";
        }
        else
            return "public-bucket";
    }
    private int getUser(){
        return 0;
    }
    private void saveDocumentToBucket(MultipartFile file, String bucketName, String objectName){
        try(InputStream inputStream = file.getInputStream()) {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName)
                    .build());
            if (!found){
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .contentType(file.getContentType())
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .build());

        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }

    }
    private void saveDocumentToDB(MultipartFile file, String bucketName, String objectName){
        documentRepository.save(Document.builder().creationDate(LocalDateTime.now())
                .ContentType(file.getContentType())
                .userId(getUser())
                .objectKey(objectName)
                .bucketName(bucketName)

                .build());
    }
    private String getDocumentUrlFromBucket(String bucketName, String objectName) {
        try {
           return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                   .bucket(bucketName)
                   .object(objectName).expiry(60*60)
                   .method(Method.GET)
                   .build()
           );
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
    }

}

