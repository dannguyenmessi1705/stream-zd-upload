package com.didan.stream.zdupload.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadSession {
    private String uploadId;
    private String fileName;
    private String contentType;
    private long fileSize;
    private long uploadedBytes;
    private int totalChunks;
    private Map<Integer, Boolean> uploadedChunks; // Map of chunkNumber to uploadStatus
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String userId; // Optional, for user-specific uploads
    private Map<String, Object> metadata; // Additional metadata provided by the client
}
