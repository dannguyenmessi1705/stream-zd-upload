package com.didan.streaming.video.repository;

import com.didan.streaming.video.entity.Livestream;
import com.didan.streaming.video.entity.LivestreamStatus;
import com.didan.streaming.video.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LivestreamRepository extends JpaRepository<Livestream, UUID> {
    Page<Livestream> findByOwner(User owner, Pageable pageable);
    Page<Livestream> findByStatus(LivestreamStatus status, Pageable pageable);
    Optional<Livestream> findByStreamKey(String streamKey);
} 