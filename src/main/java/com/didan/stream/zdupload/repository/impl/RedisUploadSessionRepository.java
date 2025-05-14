package com.didan.stream.zdupload.repository.impl;

import com.didan.stream.zdupload.exception.UploadServiceException;
import com.didan.stream.zdupload.model.UploadSession;
import com.didan.stream.zdupload.repository.UploadSessionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisUploadSessionRepository implements UploadSessionRepository {

    private static final String UPLOAD_SESSION_KEY_PREFIX = "upload:session:";
    private final RedisTemplate<String, UploadSession> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public UploadSession save(UploadSession session) {
        try {
            String key = UPLOAD_SESSION_KEY_PREFIX + session.getUploadId();
            long expiryTimeInSeconds = session.getExpiresAt().toEpochSecond(java.time.ZoneOffset.UTC) - 
                                      session.getCreatedAt().toEpochSecond(java.time.ZoneOffset.UTC);
            
            redisTemplate.opsForValue().set(key, session, expiryTimeInSeconds, TimeUnit.SECONDS);
            return session;
        } catch (Exception e) {
            log.error("Failed to save upload session: {}", e.getMessage(), e);
            throw new UploadServiceException("Failed to save upload session", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Optional<UploadSession> findById(String uploadId) {
        try {
            String key = UPLOAD_SESSION_KEY_PREFIX + uploadId;
            UploadSession session = redisTemplate.opsForValue().get(key);
            return Optional.ofNullable(session);
        } catch (Exception e) {
            log.error("Failed to find upload session with ID {}: {}", uploadId, e.getMessage(), e);
            throw new UploadServiceException("Failed to retrieve upload session", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteById(String uploadId) {
        try {
            String key = UPLOAD_SESSION_KEY_PREFIX + uploadId;
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Failed to delete upload session with ID {}: {}", uploadId, e.getMessage(), e);
            throw new UploadServiceException("Failed to delete upload session", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    @Override
    public UploadSession updateChunkStatus(String uploadId, int chunkNumber, boolean uploaded) {
        try {
            String key = UPLOAD_SESSION_KEY_PREFIX + uploadId;
            
            // Get the current session
            UploadSession session = redisTemplate.opsForValue().get(key);
            if (session == null) {
                throw new UploadServiceException("Upload session not found", HttpStatus.NOT_FOUND);
            }
            
            // Update the chunk status
            session.getUploadedChunks().put(chunkNumber, uploaded);
            
            // Recalculate uploaded bytes
            long chunkSize = session.getFileSize() / session.getTotalChunks();
            long uploadedChunksCount = session.getUploadedChunks().values().stream()
                    .filter(Boolean::booleanValue)
                    .count();
            
            // Handle last chunk which might be smaller
            long uploadedBytes = (uploadedChunksCount - 1) * chunkSize;
            if (session.getUploadedChunks().getOrDefault(session.getTotalChunks() - 1, false)) {
                uploadedBytes += session.getFileSize() % chunkSize;
            } else {
                uploadedBytes += chunkSize;
            }
            
            session.setUploadedBytes(uploadedBytes);
            
            // Save the updated session back to Redis
            long expiryTimeInSeconds = session.getExpiresAt().toEpochSecond(java.time.ZoneOffset.UTC) - 
                                      java.time.LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC);
            redisTemplate.opsForValue().set(key, session, expiryTimeInSeconds, TimeUnit.SECONDS);
            
            return session;
        } catch (UploadServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to update chunk status for upload session {}, chunk {}: {}", 
                    uploadId, chunkNumber, e.getMessage(), e);
            throw new UploadServiceException("Failed to update chunk status", e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
