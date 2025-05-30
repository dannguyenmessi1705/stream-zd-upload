package com.didan.streaming.video;

import com.didan.archetype.annotation.EnableArchetype;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableArchetype
public class StreamVideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamVideoApplication.class, args);
    }
} 