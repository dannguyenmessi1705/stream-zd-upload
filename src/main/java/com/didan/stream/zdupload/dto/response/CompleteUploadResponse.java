package com.didan.stream.zdupload.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteUploadResponse {
    private String videoId;
    private String status;
    private String objectPath;
}
