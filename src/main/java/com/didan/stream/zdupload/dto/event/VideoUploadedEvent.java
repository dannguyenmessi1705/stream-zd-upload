package com.didan.stream.zdupload.dto.event;

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
public class VideoUploadedEvent {
    private String videoId;
    private String objectPath;
    private String fileName;
    private String contentType;
    private long fileSize;
    private String userId;
    private LocalDateTime uploadedAt;
    private String checksum; // MD5 or another hash for data integrity verification
    private Map<String, Object> metadata; // Additional metadata provided during upload
}
