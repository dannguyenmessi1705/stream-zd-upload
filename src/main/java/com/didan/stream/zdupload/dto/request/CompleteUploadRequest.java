package com.didan.stream.zdupload.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteUploadRequest {
    // Optional - can be used for multipart upload completion with S3-like services
    private List<String> parts;
}
