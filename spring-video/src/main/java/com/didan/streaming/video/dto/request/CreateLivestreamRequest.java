package com.didan.streaming.video.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLivestreamRequest {
    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 3, max = 100, message = "Tiêu đề phải có độ dài từ 3 đến 100 ký tự")
    private String title;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;
} 