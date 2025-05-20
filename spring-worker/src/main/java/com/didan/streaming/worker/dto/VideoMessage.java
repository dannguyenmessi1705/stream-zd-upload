package com.didan.streaming.worker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoMessage {
    private String videoId;
    private String userId;
    private String minioPath;
    private String originalFilename;
} 