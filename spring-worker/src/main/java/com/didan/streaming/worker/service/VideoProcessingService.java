package com.didan.streaming.worker.service;

import com.didan.streaming.worker.constant.VideoStatus;
import com.didan.streaming.worker.dto.VideoMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoProcessingService {
    private final MinioService minioService;
    private final FFmpegService ffmpegService;
    private final ApiService apiService;

    @Value("${app.temp-dir}")
    private String tempDir;

    @KafkaListener(topics = "video-uploaded", groupId = "video-processor")
    public void processVideo(VideoMessage message) {
        String videoId = message.getVideoId();
        String userId = message.getUserId();
        String minioPath = message.getMinioPath();
        String originalFilename = message.getOriginalFilename();

        try {
            // Cập nhật trạng thái đang xử lý
            apiService.updateVideoStatus(videoId, VideoStatus.PROCESSING.name(), null);

            // Tạo thư mục tạm thời
            String tempPath = Path.of(tempDir, videoId).toString();
            Files.createDirectories(Path.of(tempPath));

            // Download video từ Minio
            File inputFile = new File(tempPath, originalFilename);
            minioService.downloadVideo(minioPath, inputFile.getAbsolutePath());

            // Lấy thời lượng video
            long duration = ffmpegService.getDuration(inputFile);

            // Chuyển đổi sang HLS
            String outputPath = ffmpegService.getOutputPath(UUID.fromString(userId), UUID.fromString(videoId));
            ffmpegService.convertToHls(inputFile.getAbsolutePath(), outputPath);

            // Upload HLS lên Minio
            minioService.uploadHls(outputPath, videoId);

            // Cập nhật trạng thái video thành công
            apiService.updateVideoStatus(videoId, VideoStatus.READY.name(), duration);

            // Dọn dẹp
            ffmpegService.cleanup(tempPath);
            ffmpegService.cleanup(outputPath);

        } catch (Exception e) {
            log.error("Error processing video {}: {}", videoId, e.getMessage());
            apiService.updateVideoStatus(videoId, VideoStatus.ERROR.name(), null);
            throw new RuntimeException("Failed to process video", e);
        }
    }
} 