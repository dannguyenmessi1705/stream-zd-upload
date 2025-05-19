package com.streaming.video.mapper;

import com.streaming.video.dto.VideoDto;
import com.streaming.video.entity.Video;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface VideoMapper {
    @Mapping(target = "hlsUrl", ignore = true)
    VideoDto toDto(Video video);

    @Mapping(target = "minioPath", ignore = true)
    @Mapping(target = "hlsPath", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Video toEntity(VideoDto videoDto);
} 