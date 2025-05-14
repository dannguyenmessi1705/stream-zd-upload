package com.didan.stream.zdupload.service.impl;

import com.didan.stream.zdupload.dto.event.VideoUploadedEvent;
import com.didan.stream.zdupload.dto.request.CompleteUploadRequest;
import com.didan.stream.zdupload.dto.request.InitiateUploadRequest;
import com.didan.stream.zdupload.dto.response.CompleteUploadResponse;
import com.didan.stream.zdupload.dto.response.InitiateUploadResponse;
import com.didan.stream.zdupload.dto.response.UploadStatusResponse;
import com.didan.stream.zdupload.exception.UploadServiceException;
import com.didan.stream.zdupload.model.UploadSession;
import com.didan.stream.zdupload.repository.UploadSessionRepository;
import com.didan.stream.zdupload.service.UploadService;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final UploadSessionRepository uploadSessionRepository;
    private final MinioClient minioClient;
    private final KafkaTemplate<String, VideoUploadedEvent> kafkaTemplate;
    
    @Value("${minio.bucket.name}")
    private String videoBucket;
    
    @Value("${minio.temp.bucket.name}")
    private String tempBucket;
    
    @Value("${upload.chunk-size}")
    private int chunkSize; // in bytes
    
    @Value("${upload.expiry-time}")
    private int uploadExpiryHours;
    
    @Value("${kafka.topic.video-uploaded}")
    private String videoUploadedTopic;
    
    @Override
    public InitiateUploadResponse initiateUpload(InitiateUploadRequest request, String userId) {
        try {
            // Validate file format if needed
            validateFileType(request.getFileName(), request.getContentType());
            
            // Create a new upload ID (UUID)
            String uploadId = UUID.randomUUID().toString();
            
            // Calculate total chunks based on file size and chunk size
            int totalChunks = (int) Math.ceil((double) request.getFileSize() / chunkSize);
            
            // Create a map to track uploaded chunks
            Map<Integer, Boolean> uploadedChunks = new HashMap<>();
            for (int i = 0; i < totalChunks; i++) {
                uploadedChunks.put(i, false);
            }
            
            // Create upload session with expiry time
            LocalDateTime now = LocalDateTime.now();
            UploadSession uploadSession = UploadSession.builder()
                    .uploadId(uploadId)
                    .fileName(request.getFileName())
                    .contentType(request.getContentType())
                    .fileSize(request.getFileSize())
                    .uploadedBytes(0)
                    .totalChunks(totalChunks)
                    .uploadedChunks(uploadedChunks)
                    .createdAt(now)
                    .expiresAt(now.plusHours(uploadExpiryHours))
                    .userId(userId)
                    .metadata(request.getMetadata())
                    .build();
            
            // Ensure temp bucket exists
            ensureBucketExists(tempBucket);
            
            // Save the session
            uploadSessionRepository.save(uploadSession);
            
            // Return response with upload ID and chunk size
            return InitiateUploadResponse.builder()
                    .uploadId(uploadId)
                    .chunkSize(chunkSize)
                    .totalChunks(totalChunks)
                    .build();
            
        } catch (UploadServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to initiate upload: {}", e.getMessage(), e);
            throw new UploadServiceException("Failed to initiate upload", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public boolean uploadChunk(String uploadId, int chunkNumber, MultipartFile chunk, long offset) {
        try {
            // Retrieve the upload session
            UploadSession session = uploadSessionRepository.findById(uploadId)
                    .orElseThrow(() -> new UploadServiceException("Upload session not found", HttpStatus.NOT_FOUND));
            
            // Validate chunk number
            if (chunkNumber < 0 || chunkNumber >= session.getTotalChunks()) {
                throw new UploadServiceException("Invalid chunk number", HttpStatus.BAD_REQUEST);
            }
            
            // Optional: validate offset (for extra integrity check)
            long expectedOffset = (long) chunkNumber * chunkSize;
            if (offset != expectedOffset) {
                throw new UploadServiceException("Invalid chunk offset", HttpStatus.BAD_REQUEST);
            }
            
            // Save chunk to temp storage in MinIO
            String chunkObjectName = String.format("%s/chunk_%d", uploadId, chunkNumber);
            
            try (InputStream inputStream = chunk.getInputStream()) {
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(tempBucket)
                        .object(chunkObjectName)
                        .stream(inputStream, chunk.getSize(), -1)
                        .contentType(chunk.getContentType())
                        .build());
            }
            
            // Update session status
            uploadSessionRepository.updateChunkStatus(uploadId, chunkNumber, true);
            
            return true;
            
        } catch (UploadServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to upload chunk {}: {}", chunkNumber, e.getMessage(), e);
            throw new UploadServiceException("Failed to upload chunk", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public CompleteUploadResponse completeUpload(String uploadId, CompleteUploadRequest request) {
        try {
            // Retrieve the upload session
            UploadSession session = uploadSessionRepository.findById(uploadId)
                    .orElseThrow(() -> new UploadServiceException("Upload session not found", HttpStatus.NOT_FOUND));
            
            // Verify all chunks are uploaded
            if (session.getUploadedChunks().containsValue(false)) {
                throw new UploadServiceException("Not all chunks have been uploaded", HttpStatus.BAD_REQUEST);
            }
            
            // Generate a video ID
            String videoId = UUID.randomUUID().toString();
            
            // Combine chunks (this can be implemented in different ways depending on storage solution)
            String finalObjectName = "videos/" + videoId + "/" + session.getFileName();
            combineTempChunks(session, finalObjectName);
            
            // Publish event to Kafka
            publishVideoUploadedEvent(videoId, finalObjectName, session);
            
            // Clean up temporary chunks
            cleanupTempChunks(uploadId, session.getTotalChunks());
            
            // Delete the upload session
            uploadSessionRepository.deleteById(uploadId);
            
            // Return the response
            return CompleteUploadResponse.builder()
                    .videoId(videoId)
                    .status("pending_transcode")
                    .objectPath(finalObjectName)
                    .build();
            
        } catch (UploadServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to complete upload {}: {}", uploadId, e.getMessage(), e);
            throw new UploadServiceException("Failed to complete upload", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public UploadStatusResponse getUploadStatus(String uploadId) {
        try {
            // Retrieve the upload session
            UploadSession session = uploadSessionRepository.findById(uploadId)
                    .orElseThrow(() -> new UploadServiceException("Upload session not found", HttpStatus.NOT_FOUND));
            
            // Calculate progress
            float progress = 0;
            if (session.getFileSize() > 0) {
                progress = (float) session.getUploadedBytes() / session.getFileSize() * 100;
            }
            
            // Count uploaded chunks
            long uploadedChunksCount = session.getUploadedChunks().values().stream()
                    .filter(Boolean::booleanValue)
                    .count();
            
            // Determine status
            String status = "in_progress";
            if (uploadedChunksCount == session.getTotalChunks()) {
                status = "ready_to_complete";
            }
            
            // Return the status
            return UploadStatusResponse.builder()
                    .uploadId(session.getUploadId())
                    .fileName(session.getFileName())
                    .fileSize(session.getFileSize())
                    .uploadedBytes(session.getUploadedBytes())
                    .progressPercentage(progress)
                    .totalChunks(session.getTotalChunks())
                    .uploadedChunks((int) uploadedChunksCount)
                    .status(status)
                    .expiresAt(session.getExpiresAt())
                    .chunkStatus(session.getUploadedChunks())
                    .build();
            
        } catch (UploadServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to get upload status {}: {}", uploadId, e.getMessage(), e);
            throw new UploadServiceException("Failed to get upload status", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public void abortUpload(String uploadId) {
        try {
            // Verify the upload session exists
            UploadSession session = uploadSessionRepository.findById(uploadId)
                    .orElseThrow(() -> new UploadServiceException("Upload session not found", HttpStatus.NOT_FOUND));
            
            // Clean up temporary chunks
            cleanupTempChunks(uploadId, session.getTotalChunks());
            
            // Delete the upload session
            uploadSessionRepository.deleteById(uploadId);
            
        } catch (UploadServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to abort upload {}: {}", uploadId, e.getMessage(), e);
            throw new UploadServiceException("Failed to abort upload", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Helper methods
    
    private void validateFileType(String fileName, String contentType) {
        // Example implementation - adjust as needed
        String lowerCaseFileName = fileName.toLowerCase();
        
        // Check file extension
        if (!lowerCaseFileName.endsWith(".mp4") && 
            !lowerCaseFileName.endsWith(".mov") && 
            !lowerCaseFileName.endsWith(".avi") && 
            !lowerCaseFileName.endsWith(".mkv")) {
            throw new UploadServiceException("Unsupported file format. Allowed formats: MP4, MOV, AVI, MKV", 
                    HttpStatus.BAD_REQUEST);
        }
        
        // Check MIME type
        if (!contentType.startsWith("video/")) {
            throw new UploadServiceException("Invalid content type. Only video files are allowed", 
                    HttpStatus.BAD_REQUEST);
        }
    }
    
    private void ensureBucketExists(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Created bucket: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Failed to check/create bucket {}: {}", bucketName, e.getMessage(), e);
            throw new UploadServiceException("Failed to initialize storage", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private void combineTempChunks(UploadSession session, String finalObjectName) throws Exception {
        ensureBucketExists(videoBucket);
        
        String uploadId = session.getUploadId();
        int totalChunks = session.getTotalChunks();
        
        // Method 1: For smaller files or when client-side composition is not suitable
        // We download all chunks, combine them locally, and upload the complete file
        
        Path tempDirectory = Files.createTempDirectory("video-upload-");
        Path tempFile = tempDirectory.resolve("combined-file");
        
        try {
            // Write chunks to a temporary file
            for (int i = 0; i < totalChunks; i++) {
                String chunkObjectName = String.format("%s/chunk_%d", uploadId, i);
                  try (InputStream inputStream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(tempBucket)
                                .object(chunkObjectName)
                                .build())) {
                    
                    byte[] chunkData = IOUtils.toByteArray(inputStream);
                    
                    if (i == 0) {
                        // For the first chunk, create or overwrite the file
                        Files.write(tempFile, chunkData);
                    } else {
                        // For subsequent chunks, append to the file
                        Files.write(tempFile, chunkData, StandardOpenOption.APPEND);
                    }
                }
            }
            
            // Calculate MD5 hash for integrity verification
            String md5Checksum;
            try (InputStream is = Files.newInputStream(tempFile)) {
                md5Checksum = DigestUtils.md5Hex(is);
            }
            
            // Upload combined file to final location
            minioClient.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(videoBucket)
                            .object(finalObjectName)
                            .filename(tempFile.toString())
                            .contentType(session.getContentType())
                            .build());
            
            log.info("Successfully combined and uploaded file: {}", finalObjectName);
            
            // Store the checksum in object metadata
            Map<String, String> metadata = new HashMap<>();
            metadata.put("md5", md5Checksum);
            
            minioClient.copyObject(
                    CopyObjectArgs.builder()
                            .source(CopySource.builder().bucket(videoBucket).object(finalObjectName).build())
                            .bucket(videoBucket)
                            .object(finalObjectName)
                            .headers(metadata)
                            .build());
            
        } finally {
            // Clean up temporary files
            try {
                Files.deleteIfExists(tempFile);
                Files.deleteIfExists(tempDirectory);
            } catch (IOException e) {
                log.warn("Failed to delete temporary files: {}", e.getMessage(), e);
            }
        }
        
        // Method 2: For larger services, we could use S3-compatible multipart upload directly
        // This would be more efficient for very large files but requires more complex code
        // and proper handling of the specific storage provider's API
    }
    
    private void cleanupTempChunks(String uploadId, int totalChunks) {
        for (int i = 0; i < totalChunks; i++) {
            try {
                String chunkObjectName = String.format("%s/chunk_%d", uploadId, i);
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(tempBucket)
                                .object(chunkObjectName)
                                .build());
            } catch (Exception e) {
                // Log but continue with other chunks
                log.warn("Failed to delete temp chunk {}: {}", i, e.getMessage());
            }
        }
    }
    
    private void publishVideoUploadedEvent(String videoId, String objectPath, UploadSession session) {
        VideoUploadedEvent event = VideoUploadedEvent.builder()
                .videoId(videoId)
                .objectPath(objectPath)
                .fileName(session.getFileName())
                .contentType(session.getContentType())
                .fileSize(session.getFileSize())
                .userId(session.getUserId())
                .uploadedAt(LocalDateTime.now())
                .metadata(session.getMetadata())
                .build();
        
        // Send to Kafka
        kafkaTemplate.send(videoUploadedTopic, videoId, event)
                .addCallback(
                        result -> log.info("Published VideoUploadedEvent for videoId: {}", videoId),
                        ex -> log.error("Failed to publish VideoUploadedEvent for videoId: {}: {}",
                                videoId, ex.getMessage(), ex)
                );
    }
}
