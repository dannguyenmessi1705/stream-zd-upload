package com.streaming.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoUploadedMessage {
    private UUID videoId;
    private UUID userId;
    private String minioPath;
    private String originalFilename;
    private Long fileSize;
} 