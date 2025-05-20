package com.didan.streaming.video.dto;

import com.didan.streaming.video.entity.VideoStatus;
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
public class VideoDto {
    private UUID id;
    private String title;
    private String description;
    private String originalFilename;
    private String hlsUrl;
    private Long fileSize;
    private Integer duration;
    private VideoStatus status;
    private UserDto user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 