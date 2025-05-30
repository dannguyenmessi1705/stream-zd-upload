package com.didan.streaming.video.service;

import com.didan.streaming.video.dto.VideoDto;
import com.didan.streaming.video.dto.request.CreateVideoRequest;
import com.didan.streaming.video.entity.User;
import com.didan.streaming.video.entity.Video;
import com.didan.streaming.video.entity.VideoStatus;
import com.didan.streaming.video.mapper.VideoMapper;
import com.didan.streaming.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoMapper videoMapper;
    private final StorageService storageService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("#{'${app.video.allowed-formats}'.split(',')}")
    private List<String> allowedFormats;

    @Value("${app.video.max-file-size}")
    private long maxFileSize;

    public VideoDto createVideo(CreateVideoRequest request, MultipartFile file) {
        validateVideo(file);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        var video = Video.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .originalFilename(file.getOriginalFilename())
                .status(VideoStatus.UPLOADING)
                .fileSize(file.getSize())
                .owner(currentUser)
                .build();

        var savedVideo = videoRepository.save(video);

        String minioPath = storageService.uploadVideo(file, currentUser.getId(), savedVideo.getId());
        savedVideo.setMinioPath(minioPath);
        savedVideo.setStatus(VideoStatus.UPLOADED);
        savedVideo = videoRepository.save(savedVideo);

        kafkaTemplate.send("video-uploaded", Map.of(
                "videoId", savedVideo.getId(),
                "ownerId", currentUser.getId(),
                "minioPath", minioPath,
                "originalFilename", file.getOriginalFilename()
        ));

        return videoMapper.toDto(savedVideo);
    }

    public VideoDto getVideo(UUID id) {
        var video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy video"));
        return videoMapper.toDto(video);
    }

    public Page<VideoDto> getVideos(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return videoRepository.findByOwner(currentUser, pageable)
                .map(videoMapper::toDto);
    }

    public void deleteVideo(UUID id) {
        var video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy video"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!video.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa video này");
        }

        storageService.deleteVideo(video.getMinioPath());
        if (video.getHlsPath() != null) {
            storageService.deleteHls(video.getHlsPath());
        }

        videoRepository.delete(video);
    }

    private void validateVideo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File không được để trống");
        }

        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList(allowedFormats).contains(contentType)) {
            throw new RuntimeException("Định dạng file không được hỗ trợ");
        }

        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("Kích thước file vượt quá giới hạn cho phép");
        }
    }

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(videoMapper::toDto)
                .toList();
    }

    public VideoDto getVideoById(UUID id) {
        return videoRepository.findById(id)
                .map(videoMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Video not found"));
    }

    public VideoDto uploadVideo(MultipartFile file, String title, String description) throws IOException {
        validateVideo(file);

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Video video = Video.builder()
                .title(title)
                .description(description)
                .originalFilename(file.getOriginalFilename())
                .status(VideoStatus.UPLOADING)
                .owner(currentUser)
                .build();

        video = videoRepository.save(video);

        String minioPath = storageService.uploadVideo(file, currentUser.getId(), video.getId());
        video.setMinioPath(minioPath);
        video.setFileSize(file.getSize());
        video.setStatus(VideoStatus.UPLOADED);

        video = videoRepository.save(video);
        
        return videoMapper.toDto(video);
    }

    private UUID getCurrentUserId() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return currentUser.getId();
    }
} 