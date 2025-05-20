package com.didan.streaming.video.mapper;

import com.didan.streaming.video.dto.UserDto;
import com.didan.streaming.video.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDto toDto(User user);

    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    User toEntity(UserDto dto);

    @Named("roleToAuthorities")
    default List<String> roleToAuthorities(com.didan.streaming.video.entity.Role role) {
        if (role == null) return List.of();
        return List.of("ROLE_" + role.name());
    }
} 