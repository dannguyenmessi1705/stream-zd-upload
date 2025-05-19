package com.streaming.video.controller;

import com.streaming.video.dto.LivestreamDto;
import com.streaming.video.dto.request.CreateLivestreamRequest;
import com.streaming.video.service.LivestreamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/livestreams")
@RequiredArgsConstructor
public class LivestreamController {

    private final LivestreamService livestreamService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<LivestreamDto> createLivestream(@Valid @RequestBody CreateLivestreamRequest request) {
        return ResponseEntity.ok(livestreamService.createLivestream(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivestreamDto> getLivestream(@PathVariable UUID id) {
        return ResponseEntity.ok(livestreamService.getLivestream(id));
    }

    @GetMapping
    public ResponseEntity<Page<LivestreamDto>> getLivestreams(Pageable pageable) {
        return ResponseEntity.ok(livestreamService.getLivestreams(pageable));
    }

    @PostMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> startLivestream(@PathVariable UUID id) {
        livestreamService.startLivestream(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/end")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> endLivestream(@PathVariable UUID id) {
        livestreamService.endLivestream(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteLivestream(@PathVariable UUID id) {
        livestreamService.deleteLivestream(id);
        return ResponseEntity.noContent().build();
    }
} 