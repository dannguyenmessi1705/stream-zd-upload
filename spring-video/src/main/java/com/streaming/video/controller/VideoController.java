package com.streaming.video.controller;

import com.streaming.video.dto.VideoDto;
import com.streaming.video.dto.request.CreateVideoRequest;
import com.streaming.video.service.VideoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteVideo(@PathVariable UUID id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
} 