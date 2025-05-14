package com.didan.stream.zdupload.controller;

import com.didan.stream.zdupload.dto.request.CompleteUploadRequest;
import com.didan.stream.zdupload.dto.request.InitiateUploadRequest;
import com.didan.stream.zdupload.dto.response.CompleteUploadResponse;
import com.didan.stream.zdupload.dto.response.InitiateUploadResponse;
import com.didan.stream.zdupload.dto.response.UploadStatusResponse;
import com.didan.stream.zdupload.service.UploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/uploads")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/initiate")
    public ResponseEntity<InitiateUploadResponse> initiateUpload(
            @Valid @RequestBody InitiateUploadRequest request,
            @RequestHeader(value = "X-User-Id", required = false) String userId) {
        
        log.info("Initiating upload for file: {}, size: {}", request.getFileName(), request.getFileSize());
        InitiateUploadResponse response = uploadService.initiateUpload(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{uploadId}/chunk")
    public ResponseEntity<Void> uploadChunk(
            @PathVariable String uploadId,
            @RequestParam("chunkNumber") int chunkNumber,
            @RequestParam("offset") long offset,
            @RequestParam("file") MultipartFile file) {
        
        log.info("Uploading chunk {} for upload ID: {}, offset: {}", chunkNumber, uploadId, offset);
        boolean success = uploadService.uploadChunk(uploadId, chunkNumber, file, offset);
        
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/{uploadId}/complete")
    public ResponseEntity<CompleteUploadResponse> completeUpload(
            @PathVariable String uploadId,
            @RequestBody(required = false) CompleteUploadRequest request) {
        
        log.info("Completing upload for ID: {}", uploadId);
        CompleteUploadResponse response = uploadService.completeUpload(uploadId, request != null ? request : new CompleteUploadRequest());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{uploadId}/status")
    public ResponseEntity<UploadStatusResponse> getUploadStatus(@PathVariable String uploadId) {
        log.info("Getting status for upload ID: {}", uploadId);
        UploadStatusResponse response = uploadService.getUploadStatus(uploadId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{uploadId}")
    public ResponseEntity<Void> abortUpload(@PathVariable String uploadId) {
        log.info("Aborting upload for ID: {}", uploadId);
        uploadService.abortUpload(uploadId);
        return ResponseEntity.noContent().build();
    }
}
