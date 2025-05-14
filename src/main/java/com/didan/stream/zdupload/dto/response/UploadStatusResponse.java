package com.didan.stream.zdupload.dto.response;

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
public class UploadStatusResponse {
    private String uploadId;
    private String fileName;
    private long fileSize;
    private long uploadedBytes;
    private float progressPercentage;
    private int totalChunks;
    private int uploadedChunks;
    private String status;
    private LocalDateTime expiresAt;
    private Map<Integer, Boolean> chunkStatus; // Optional: detailed status of each chunk
}
