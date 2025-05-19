package com.streaming.worker.service;

import com.streaming.worker.dto.VideoProcessedMessage;
import com.streaming.worker.dto.VideoUploadedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoProcessingService {

    private final StorageService storageService;
    private final FFmpegService ffmpegService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.ffmpeg.input-path}")
    private String inputPath;

    @KafkaListener(topics = "${kafka.topic.video-uploaded}")
    public void handleVideoUploaded(VideoUploadedMessage message) {
        log.info("Received video uploaded message: {}", message);
        
        try {
            // Download video from MinIO
            String videoPath = Path.of(inputPath, message.getVideoId().toString()).toString();
            byte[] videoData = storageService.getVideo(message.getMinioPath());
            Files.write(Path.of(videoPath), videoData);
            File videoFile = new File(videoPath);

            // Convert to HLS
            String hlsPath = ffmpegService.convertToHls(videoFile, message.getVideoId());
            
            // Get video duration
            long duration = ffmpegService.getDuration(videoFile);

            // Upload HLS files to MinIO
            File hlsDir = new File(Path.of(ffmpegService.getOutputPath(), hlsPath).toString());
            for (File file : hlsDir.listFiles()) {
                byte[] data = Files.readAllBytes(file.toPath());
                storageService.uploadHls(Path.of(hlsPath, file.getName()).toString(), data);
            }

            // Delete temporary files
            videoFile.delete();
            deleteDirectory(hlsDir);

            // Send success message
            VideoProcessedMessage processedMessage = VideoProcessedMessage.builder()
                    .videoId(message.getVideoId())
                    .hlsPath(hlsPath)
                    .duration(duration)
                    .success(true)
                    .build();
            kafkaTemplate.send("video-processed", processedMessage);

        } catch (Exception e) {
            log.error("Error processing video: {}", e.getMessage(), e);
            
            // Send error message
            VideoProcessedMessage errorMessage = VideoProcessedMessage.builder()
                    .videoId(message.getVideoId())
                    .success(false)
                    .errorMessage(e.getMessage())
                    .build();
            kafkaTemplate.send("video-processed", errorMessage);
        }
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
} 