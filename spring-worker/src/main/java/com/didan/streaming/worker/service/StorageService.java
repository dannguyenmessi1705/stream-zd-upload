package com.didan.streaming.worker.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final MinioClient minioClient;

    @Value("${app.minio.bucket.video}")
    private String videoBucket;

    @Value("${app.minio.bucket.hls}")
    private String hlsBucket;

    public byte[] getVideo(String objectName) {
        try {
            GetObjectResponse response = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(videoBucket)
                            .object(objectName)
                            .build()
            );
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Error getting video from MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get video from storage", e);
        }
    }

    public void uploadHls(String objectName, byte[] data) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(hlsBucket)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(data), data.length, -1)
                            .contentType("application/x-mpegURL")
                            .build()
            );
        } catch (Exception e) {
            log.error("Error uploading HLS file to MinIO: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to upload HLS file to storage", e);
        }
    }
} 