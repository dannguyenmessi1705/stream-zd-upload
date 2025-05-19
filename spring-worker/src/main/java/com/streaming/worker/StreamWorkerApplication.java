package com.streaming.worker;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

import java.io.File;
import java.nio.file.Path;

@SpringBootApplication
@EnableKafka
public class StreamWorkerApplication {

    @Value("${app.ffmpeg.input-path}")
    private String inputPath;

    @Value("${app.ffmpeg.output-path}")
    private String outputPath;

    public static void main(String[] args) {
        SpringApplication.run(StreamWorkerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Tạo thư mục làm việc
        createDirectoryIfNotExists(inputPath);
        createDirectoryIfNotExists(outputPath);
    }

    private void createDirectoryIfNotExists(String path) {
        File directory = Path.of(path).toFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
} 