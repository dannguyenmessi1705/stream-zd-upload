package com.didan.stream.zdupload.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UploadServiceException extends RuntimeException {
    private final HttpStatus status;
    
    public UploadServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    
    public UploadServiceException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
