package com.didan.streaming.video.service;

import io.debezium.engine.DebeziumEngine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebeziumService {

    private final DebeziumEngine<?> debeziumEngine;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void start() {
        try {
            log.info("Starting Debezium engine...");
            this.executor.execute(debeziumEngine);
        } catch (Exception e) {
            log.error("Error starting Debezium engine: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to start Debezium engine", e);
        }
    }

    @PreDestroy
    private void stop() {
        try {
            log.info("Stopping Debezium engine...");
            if (this.debeziumEngine != null) {
                this.debeziumEngine.close();
            }
        } catch (IOException e) {
            log.error("Error stopping Debezium engine: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to stop Debezium engine", e);
        }
    }
} 