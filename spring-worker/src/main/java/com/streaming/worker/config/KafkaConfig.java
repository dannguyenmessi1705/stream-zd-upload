package com.streaming.worker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.video-uploaded}")
    private String videoUploadedTopic;

    @Value("${kafka.topic.video-processed}")
    private String videoProcessedTopic;

    @Bean
    public NewTopic videoUploadedTopic() {
        return TopicBuilder.name(videoUploadedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic videoProcessedTopic() {
        return TopicBuilder.name(videoProcessedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
} 