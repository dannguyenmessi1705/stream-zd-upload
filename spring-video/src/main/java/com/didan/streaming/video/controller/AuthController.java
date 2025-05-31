package com.didan.streaming.video.controller;

import com.didan.archetype.config.kafka.PushDataToKafkaUtils;
import com.didan.archetype.factory.response.GeneralResponse;
import com.didan.archetype.factory.response.ResponseFactory;
import com.didan.streaming.video.dto.request.LoginRequest;
import com.didan.streaming.video.dto.request.RegisterRequest;
import com.didan.streaming.video.dto.response.AuthResponse;
import com.didan.streaming.video.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ResponseFactory responseFactory;

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return responseFactory.success(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        return responseFactory.success(authService.login(request));
    }
} 