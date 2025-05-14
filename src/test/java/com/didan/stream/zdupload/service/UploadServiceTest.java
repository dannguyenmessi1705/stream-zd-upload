package com.didan.stream.zdupload.service;

import com.didan.stream.zdupload.dto.request.InitiateUploadRequest;
import com.didan.stream.zdupload.dto.response.InitiateUploadResponse;
import com.didan.stream.zdupload.repository.UploadSessionRepository;
import com.didan.stream.zdupload.service.impl.UploadServiceImpl;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UploadServiceTest {

    @Mock
    private UploadSessionRepository uploadSessionRepository;
    
    @Mock
    private MinioClient minioClient;
    
    @Mock
    private KafkaTemplate kafkaTemplate;
    
    @InjectMocks
    private UploadServiceImpl uploadService;
    
    @Test
    public void testInitiateUpload() {
        // Set the required field values directly using ReflectionTestUtils
        ReflectionTestUtils.setField(uploadService, "videoBucket", "video-bucket");
        ReflectionTestUtils.setField(uploadService, "tempBucket", "temp-bucket");
        ReflectionTestUtils.setField(uploadService, "chunkSize", 8388608); // 8MB
        ReflectionTestUtils.setField(uploadService, "uploadExpiryHours", 24);
        
        // Mock behaviors
        when(uploadSessionRepository.save(any())).thenReturn(null);
        
        // Create request
        InitiateUploadRequest request = new InitiateUploadRequest();
        request.setFileName("test-video.mp4");
        request.setFileSize(104857600L); // 100MB
        request.setContentType("video/mp4");
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("title", "Test Video");
        request.setMetadata(metadata);
        
        // Execute
        InitiateUploadResponse response = uploadService.initiateUpload(request, "user-123");
        
        // Verify
        assertNotNull(response);
        assertNotNull(response.getUploadId());
        assertEquals(8388608, response.getChunkSize());
        assertEquals(13, response.getTotalChunks()); // 100MB / 8MB = 12.5 → 13 chunks
        
        // Verify repository was called
        verify(uploadSessionRepository, times(1)).save(any());
    }
}
