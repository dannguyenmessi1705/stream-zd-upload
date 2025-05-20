package com.didan.streaming.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    private final RestTemplate restTemplate;

    @Value("${app.api.base-url}")
    private String baseUrl;

    @Value("${app.api.key}")
    private String apiKey;

    public void updateVideoStatus(String videoId, String status, Long duration) {
        try {
            String url = baseUrl + "/api/videos/" + videoId + "/status";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-API-KEY", apiKey);

            Map<String, Object> body = Map.of(
                    "status", status,
                    "duration", duration != null ? duration : 0
            );

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.postForObject(url, request, Void.class);

            log.info("Updated video status: videoId={}, status={}, duration={}", videoId, status, duration);
        } catch (Exception e) {
            log.error("Error updating video status: {}", e.getMessage());
            throw new RuntimeException("Failed to update video status", e);
        }
    }
} 