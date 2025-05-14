package com.didan.stream.zdupload.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.video-uploaded}")
    private String videoUploadedTopic;

    @Bean
    public NewTopic videoUploadedTopic() {
        return TopicBuilder.name(videoUploadedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
