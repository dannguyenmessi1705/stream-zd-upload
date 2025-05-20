package com.didan.streaming.video.repository;

import com.didan.streaming.video.entity.User;
import com.didan.streaming.video.entity.Video;
import com.didan.streaming.video.entity.VideoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VideoRepository extends JpaRepository<Video, UUID> {
    Page<Video> findByOwner(User owner, Pageable pageable);
    Page<Video> findByStatus(VideoStatus status, Pageable pageable);
    List<Video> findByStatusIn(List<VideoStatus> statuses);
} 