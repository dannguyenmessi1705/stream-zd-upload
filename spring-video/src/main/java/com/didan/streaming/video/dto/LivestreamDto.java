package com.didan.streaming.video.dto;

import com.didan.streaming.video.entity.LivestreamStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivestreamDto {
    private UUID id;
    private String title;
    private String description;
    private String streamKey;
    private String rtmpUrl;
    private String webrtcUrl;
    private String hlsUrl;
    private LivestreamStatus status;
    private UserDto owner;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 