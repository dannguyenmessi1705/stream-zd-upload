package com.didan.streaming.video.mapper;

import com.didan.streaming.video.dto.LivestreamDto;
import com.didan.streaming.video.entity.Livestream;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {UserMapper.class})
public interface LivestreamMapper {
    LivestreamDto toDto(Livestream livestream);
    Livestream toEntity(LivestreamDto livestreamDto);
} 