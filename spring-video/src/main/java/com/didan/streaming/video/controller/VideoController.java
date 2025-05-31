package com.didan.streaming.video.controller;

import com.didan.streaming.video.dto.VideoDto;
import com.didan.streaming.video.dto.request.CreateVideoRequest;
import com.didan.streaming.video.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoDto> createVideo(
            @Valid @RequestPart("request") CreateVideoRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        return ResponseEntity.ok(videoService.createVideo(request, file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDto> getVideo(@PathVariable UUID id) {
        return ResponseEntity.ok(videoService.getVideo(id));
    }

    @GetMapping
    public ResponseEntity<Page<VideoDto>> getVideos(Pageable pageable) {
        return ResponseEntity.ok(videoService.getVideos(pageable));
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Void> updateVideoStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, Object> statusUpdate,
            @RequestHeader("X-API-KEY") String apiKey
    ) {
        String status = (String) statusUpdate.get("status");
        Integer duration = statusUpdate.get("duration") != null ?
                Integer.valueOf(statusUpdate.get("duration").toString()) : null;

        videoService.updateVideoStatus(id, status, duration);
        return ResponseEntity.ok().build();
    }
}

