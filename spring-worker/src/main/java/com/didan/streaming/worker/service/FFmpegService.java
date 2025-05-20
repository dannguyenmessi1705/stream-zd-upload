package com.didan.streaming.worker.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FFmpegService {
    @Value("${app.ffmpeg.output-dir}")
    private String outputDir;

    @Value("${app.ffmpeg.segment-duration}")
    private int segmentDuration;

    public String getOutputPath(UUID userId, UUID videoId) {
        return String.format("%s/%s/%s", outputDir, userId, videoId);
    }

    public void convertToHls(String inputPath, String outputPath) throws IOException {
        // Tạo thư mục output nếu chưa tồn tại
        Files.createDirectories(Path.of(outputPath));

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(inputPath);
        command.add("-c:v");
        command.add("libx264");
        command.add("-c:a");
        command.add("aac");
        command.add("-hls_time");
        command.add(String.valueOf(segmentDuration));
        command.add("-hls_list_size");
        command.add("0");
        command.add("-hls_segment_filename");
        command.add(outputPath + "/segment_%03d.ts");
        command.add(outputPath + "/playlist.m3u8");

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);

        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg process failed with exit code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("FFmpeg process was interrupted", e);
        }
    }

    public void cleanup(String path) {
        try {
            File directory = new File(path);
            if (directory.exists()) {
                Files.walk(directory.toPath())
                        .sorted((p1, p2) -> -p1.compareTo(p2))
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            log.error("Error cleaning up directory {}: {}", path, e.getMessage());
            throw new RuntimeException("Failed to cleanup directory", e);
        }
    }

    public long getDuration(File inputFile) throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("ffprobe");
        command.add("-v");
        command.add("error");
        command.add("-show_entries");
        command.add("format=duration");
        command.add("-of");
        command.add("default=noprint_wrappers=1:nokey=1");
        command.add(inputFile.getAbsolutePath());

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        byte[] output = process.getInputStream().readAllBytes();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new IOException("FFprobe process failed with exit code: " + exitCode);
        }

        String duration = new String(output).trim();
        return (long) (Double.parseDouble(duration) * 1000); // Convert to milliseconds
    }
} 