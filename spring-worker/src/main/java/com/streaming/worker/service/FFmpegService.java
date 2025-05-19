package com.streaming.worker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FFmpegService {

    @Value("${app.ffmpeg.output-path}")
    private String outputPath;

    public String convertToHls(File inputFile, UUID videoId) throws IOException, InterruptedException {
        String outputDir = Path.of(outputPath, videoId.toString()).toString();
        new File(outputDir).mkdirs();

        String playlistFile = Path.of(outputDir, "playlist.m3u8").toString();
        
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-i");
        command.add(inputFile.getAbsolutePath());
        command.add("-profile:v");
        command.add("baseline");
        command.add("-level");
        command.add("3.0");
        command.add("-start_number");
        command.add("0");
        command.add("-hls_time");
        command.add("10");
        command.add("-hls_list_size");
        command.add("0");
        command.add("-f");
        command.add("hls");
        command.add(playlistFile);

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("FFmpeg process failed with exit code: " + exitCode);
        }

        return videoId.toString();
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