package com.didan.streaming.video.dto;

import com.didan.streaming.video.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 