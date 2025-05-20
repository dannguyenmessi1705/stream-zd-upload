package com.didan.streaming.video.mapper;

import com.didan.streaming.video.dto.LivestreamDto;
import com.didan.streaming.video.entity.Livestream;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface LivestreamMapper {
    @Mapping(target = "rtmpUrl", ignore = true)
    @Mapping(target = "webrtcUrl", ignore = true)
    @Mapping(target = "hlsUrl", ignore = true)
    LivestreamDto toDto(Livestream livestream);

    @Mapping(target = "streamKey", ignore = true)
    @Mapping(target = "rtmpUrl", ignore = true)
    @Mapping(target = "webrtcUrl", ignore = true)
    @Mapping(target = "hlsUrl", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "endedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Livestream toEntity(LivestreamDto livestreamDto);
} 