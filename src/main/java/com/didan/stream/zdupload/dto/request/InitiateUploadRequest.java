package com.didan.stream.zdupload.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiateUploadRequest {
    @NotBlank(message = "File name must not be blank")
    private String fileName;
    
    @NotNull(message = "File size must be provided")
    @Min(value = 1, message = "File size must be greater than zero")
    private Long fileSize;
    
    @NotBlank(message = "Content type must not be blank")
    private String contentType;
    
    private Map<String, Object> metadata;
}
