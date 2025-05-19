package com.streaming.video.service;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final MinioClient minioClient;

    @Value("${minio.bucket.video}")
    private String videoBucket;

    @Value("${minio.bucket.hls}")
    private String hlsBucket;

    public String uploadVideo(MultipartFile file, UUID userId, UUID videoId) {
        try {
            String objectName = String.format("%s/%s/%s", userId, videoId, file.getOriginalFilename());
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
            return objectName;
        } catch (Exception e) {
            log.error("Lỗi khi upload video: {}", e.getMessage());
            throw new RuntimeException("Không thể upload video");
        }
    }

    public void uploadHls(String hlsPath, byte[] data) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(hlsPath)
                    .stream(new ByteArrayInputStream(data), data.length, -1)
                    .contentType("application/x-mpegURL")
                    .build());
        } catch (Exception e) {
            log.error("Lỗi khi upload HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể upload HLS");
        }
    }

    public byte[] getVideo(String objectName) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .build());
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Lỗi khi lấy video: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy video");
        }
    }

    public byte[] getHls(String objectName) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .build());
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Lỗi khi lấy HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy HLS");
        }
    }

    public List<String> listHlsFiles(String prefix) {
        try {
            List<String> result = new ArrayList<>();
            Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(hlsBucket)
                    .prefix(prefix)
                    .recursive(true)
                    .build());
            for (Result<Item> object : objects) {
                result.add(object.get().objectName());
            }
            return result;
        } catch (Exception e) {
            log.error("Lỗi khi liệt kê file HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể liệt kê file HLS");
        }
    }

    public void deleteVideo(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("Lỗi khi xóa video: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa video");
        }
    }

    public void deleteHls(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("Lỗi khi xóa HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa HLS");
        }
    }
} 