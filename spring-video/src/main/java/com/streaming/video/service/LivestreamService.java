package com.streaming.video.service;

import com.streaming.video.dto.LivestreamDto;
import com.streaming.video.dto.request.CreateLivestreamRequest;
import com.streaming.video.entity.Livestream;
import com.streaming.video.entity.LivestreamStatus;
import com.streaming.video.entity.User;
import com.streaming.video.mapper.LivestreamMapper;
import com.streaming.video.repository.LivestreamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LivestreamService {
    private final LivestreamRepository livestreamRepository;
    private final LivestreamMapper livestreamMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.rtmp.url}")
    private String rtmpUrl;

    @Value("${app.webrtc.url}")
    private String webrtcUrl;

    public LivestreamDto createLivestream(CreateLivestreamRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String streamKey = UUID.randomUUID().toString();

        var livestream = Livestream.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .streamKey(streamKey)
                .rtmpUrl(rtmpUrl + "/" + streamKey)
                .webrtcUrl(webrtcUrl + "/" + streamKey)
                .status(LivestreamStatus.CREATED)
                .owner(currentUser)
                .build();

        var savedLivestream = livestreamRepository.save(livestream);

        kafkaTemplate.send("livestream-status", Map.of(
                "livestreamId", savedLivestream.getId(),
                "ownerId", currentUser.getId(),
                "status", LivestreamStatus.CREATED,
                "streamKey", streamKey
        ));

        return livestreamMapper.toDto(savedLivestream);
    }

    public LivestreamDto getLivestream(UUID id) {
        var livestream = livestreamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy livestream"));
        return livestreamMapper.toDto(livestream);
    }

    public Page<LivestreamDto> getLivestreams(Pageable pageable) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return livestreamRepository.findByOwner(currentUser, pageable)
                .map(livestreamMapper::toDto);
    }

    public void startLivestream(UUID id) {
        var livestream = livestreamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy livestream"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!livestream.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền bắt đầu livestream này");
        }

        if (livestream.getStatus() != LivestreamStatus.CREATED) {
            throw new RuntimeException("Livestream không ở trạng thái có thể bắt đầu");
        }

        livestream.setStatus(LivestreamStatus.LIVE);
        livestream.setStartedAt(LocalDateTime.now());
        livestreamRepository.save(livestream);

        kafkaTemplate.send("livestream-status", Map.of(
                "livestreamId", livestream.getId(),
                "ownerId", currentUser.getId(),
                "status", LivestreamStatus.LIVE,
                "streamKey", livestream.getStreamKey()
        ));
    }

    public void endLivestream(UUID id) {
        var livestream = livestreamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy livestream"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!livestream.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền kết thúc livestream này");
        }

        if (livestream.getStatus() != LivestreamStatus.LIVE) {
            throw new RuntimeException("Livestream không ở trạng thái có thể kết thúc");
        }

        livestream.setStatus(LivestreamStatus.ENDED);
        livestream.setEndedAt(LocalDateTime.now());
        livestreamRepository.save(livestream);

        kafkaTemplate.send("livestream-status", Map.of(
                "livestreamId", livestream.getId(),
                "ownerId", currentUser.getId(),
                "status", LivestreamStatus.ENDED,
                "streamKey", livestream.getStreamKey()
        ));
    }

    public void deleteLivestream(UUID id) {
        var livestream = livestreamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy livestream"));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!livestream.getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Bạn không có quyền xóa livestream này");
        }

        if (livestream.getStatus() == LivestreamStatus.LIVE) {
            throw new RuntimeException("Không thể xóa livestream đang diễn ra");
        }

        livestreamRepository.delete(livestream);
    }
} 