package com.didan.streaming.video.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class
KafkaConfigTopic {

    @Value("${spring.kafka.producer.topic.video-uploaded}")
    private String videoUploadedTopicName;
    @Value("${spring.kafka.producer.topic.video-processed}")
    private String videoProcessedTopicName;
    @Value("${spring.kafka.producer.topic.livestream-status}")
    private String livestreamStatusTopicName;

    @Bean
    public NewTopic videoUploadedTopic() {
        return TopicBuilder.name(videoUploadedTopicName)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic videoProcessedTopic() {
        return TopicBuilder.name(videoProcessedTopicName)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic livestreamStatusTopic() {
        return TopicBuilder.name(livestreamStatusTopicName)
                .partitions(3)
                .replicas(1)
                .build();
    }
} 