package com.streaming.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String VIDEO_CACHE_PREFIX = "video:";
    private static final String LIVESTREAM_CACHE_PREFIX = "livestream:";

    public void invalidateVideoCache(String id) {
        try {
            String key = VIDEO_CACHE_PREFIX + id;
            redisTemplate.delete(key);
            log.info("Invalidated video cache: {}", id);
        } catch (Exception e) {
            log.error("Error invalidating video cache: {}", e.getMessage(), e);
        }
    }

    public void invalidateLivestreamCache(String id) {
        try {
            String key = LIVESTREAM_CACHE_PREFIX + id;
            redisTemplate.delete(key);
            log.info("Invalidated livestream cache: {}", id);
        } catch (Exception e) {
            log.error("Error invalidating livestream cache: {}", e.getMessage(), e);
        }
    }
} 