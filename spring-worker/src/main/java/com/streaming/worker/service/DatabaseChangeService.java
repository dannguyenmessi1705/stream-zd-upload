package com.streaming.worker.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseChangeService {

    private final ObjectMapper objectMapper;
    private final CacheService cacheService;

    @KafkaListener(topics = "db-changes.videos")
    public void handleVideoChanges(Map<String, Object> message) {
        try {
            String operation = (String) message.get("operation");
            JsonNode before = objectMapper.convertValue(message.get("before"), JsonNode.class);
            JsonNode after = objectMapper.convertValue(message.get("after"), JsonNode.class);

            switch (operation) {
                case "c": // Create
                case "u": // Update
                    cacheService.invalidateVideoCache(after.get("id").asText());
                    break;
                case "d": // Delete
                    cacheService.invalidateVideoCache(before.get("id").asText());
                    break;
                default:
                    log.warn("Unknown operation type: {}", operation);
            }
        } catch (Exception e) {
            log.error("Error handling video change: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "db-changes.livestreams")
    public void handleLivestreamChanges(Map<String, Object> message) {
        try {
            String operation = (String) message.get("operation");
            JsonNode before = objectMapper.convertValue(message.get("before"), JsonNode.class);
            JsonNode after = objectMapper.convertValue(message.get("after"), JsonNode.class);

            switch (operation) {
                case "c": // Create
                case "u": // Update
                    cacheService.invalidateLivestreamCache(after.get("id").asText());
                    break;
                case "d": // Delete
                    cacheService.invalidateLivestreamCache(before.get("id").asText());
                    break;
                default:
                    log.warn("Unknown operation type: {}", operation);
            }
        } catch (Exception e) {
            log.error("Error handling livestream change: {}", e.getMessage(), e);
        }
    }
} 