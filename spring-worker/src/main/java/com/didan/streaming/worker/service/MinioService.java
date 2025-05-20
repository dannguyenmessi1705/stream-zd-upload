package com.didan.streaming.worker.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.video}")
    private String videoBucket;

    @Value("${minio.bucket.hls}")
    private String hlsBucket;

    public void downloadVideo(String minioPath, String localPath) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(minioPath)
                    .build());

            try (FileOutputStream outputStream = new FileOutputStream(localPath)) {
                response.transferTo(outputStream);
            }
        } catch (Exception e) {
            log.error("Error downloading video from Minio: {}", e.getMessage());
            throw new RuntimeException("Failed to download video from Minio", e);
        }
    }

    public void uploadHls(String hlsPath, String videoId) {
        try {
            File hlsDir = new File(hlsPath);
            if (!hlsDir.exists() || !hlsDir.isDirectory()) {
                throw new RuntimeException("HLS directory not found: " + hlsPath);
            }

            Files.walk(hlsDir.toPath())
                    .filter(Files::isRegularFile)
                    .forEach(path -> uploadHlsFile(path, videoId));

        } catch (IOException e) {
            log.error("Error uploading HLS files to Minio: {}", e.getMessage());
            throw new RuntimeException("Failed to upload HLS files to Minio", e);
        }
    }

    private void uploadHlsFile(Path filePath, String videoId) {
        try {
            String fileName = filePath.getFileName().toString();
            String objectName = String.format("%s/%s", videoId, fileName);

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .stream(new FileInputStream(filePath.toFile()), Files.size(filePath), -1)
                    .contentType(getContentType(fileName))
                    .build());
        } catch (Exception e) {
            log.error("Error uploading HLS file to Minio: {}", e.getMessage());
            throw new RuntimeException("Failed to upload HLS file to Minio", e);
        }
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".m3u8")) {
            return "application/x-mpegURL";
        } else if (fileName.endsWith(".ts")) {
            return "video/MP2T";
        } else {
            return "application/octet-stream";
        }
    }
} 