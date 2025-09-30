package com.filesmanagement.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Value("${minio.url}")
    private String url;
    @Value("${minio.access.name}")
    private String name;
    @Value("${minio.access.secret}")
    private String secret;

    @Bean
    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(url).credentials(name, secret).build();
    }

}
