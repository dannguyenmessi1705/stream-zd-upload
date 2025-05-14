package com.didan.stream.zdupload.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiateUploadResponse {
    private String uploadId;
    private int chunkSize;
    private int totalChunks;
}
