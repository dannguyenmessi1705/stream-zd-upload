package com.didan.streaming.video.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebeziumListener implements DebeziumEngine.ChangeConsumer<ChangeEvent<String, String>> {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void handleBatch(java.util.List<ChangeEvent<String, String>> records,
                          DebeziumEngine.RecordCommitter<ChangeEvent<String, String>> committer) throws InterruptedException {
        for (ChangeEvent<String, String> record : records) {
            try {
                handleChangeEvent(record);
                committer.markProcessed(record);
            } catch (Exception e) {
                log.error("Error processing record: {}", e.getMessage(), e);
            }
        }
        committer.markBatchFinished();
    }

    private void handleChangeEvent(ChangeEvent<String, String> record) throws Exception {
        if (record.value() == null) {
            return;
        }

        JsonNode payload = objectMapper.readTree(record.value()).get("payload");
        String operation = payload.get("op").asText();
        String table = payload.get("source").get("table").asText();

        Map<String, Object> message = Map.of(
                "operation", operation,
                "table", table,
                "before", payload.get("before"),
                "after", payload.get("after")
        );

        String topic = String.format("db-changes.%s", table);
        kafkaTemplate.send(topic, message);
        log.info("Sent database change event to topic {}: {}", topic, message);
    }
} 