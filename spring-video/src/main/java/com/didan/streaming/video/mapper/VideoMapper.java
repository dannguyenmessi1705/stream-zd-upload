package com.didan.streaming.video.mapper;

import com.didan.streaming.video.dto.VideoDto;
import com.didan.streaming.video.entity.Video;
import com.didan.streaming.video.entity.VideoStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface VideoMapper {
    
    @Mapping(target = "hlsUrl", source = "hlsPath", qualifiedByName = "toHlsUrl")
    @Mapping(target = "user", source = "owner")
    VideoDto toDto(Video video);

    @Mapping(target = "hlsPath", ignore = true)
    @Mapping(target = "minioPath", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "status", constant = "UPLOADING")
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "owner", source = "user")
    Video toEntity(VideoDto dto);

    @Named("toHlsUrl")
    default String toHlsUrl(String hlsPath) {
        if (hlsPath == null) return null;
        return "/api/videos/hls/" + hlsPath;
    }

    default VideoStatus defaultStatus() {
        return VideoStatus.UPLOADING;
    }
} 