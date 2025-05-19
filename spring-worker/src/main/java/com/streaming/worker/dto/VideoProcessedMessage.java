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
public class VideoProcessedMessage {
    private UUID videoId;
    private String hlsPath;
    private Long duration;
    private boolean success;
    private String errorMessage;
} 