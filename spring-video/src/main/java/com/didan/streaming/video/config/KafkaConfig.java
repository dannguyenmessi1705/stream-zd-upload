package com.didan.streaming.video.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic videoUploadedTopic() {
        return TopicBuilder.name("video-uploaded")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic videoProcessedTopic() {
        return TopicBuilder.name("video-processed")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic livestreamStatusTopic() {
        return TopicBuilder.name("livestream-status")
                .partitions(3)
                .replicas(1)
                .build();
    }
} 