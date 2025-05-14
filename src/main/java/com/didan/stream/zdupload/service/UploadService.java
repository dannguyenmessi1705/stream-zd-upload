package com.didan.stream.zdupload.service;

import com.didan.stream.zdupload.dto.event.VideoUploadedEvent;
import com.didan.stream.zdupload.dto.request.CompleteUploadRequest;
import com.didan.stream.zdupload.dto.request.InitiateUploadRequest;
import com.didan.stream.zdupload.dto.response.CompleteUploadResponse;
import com.didan.stream.zdupload.dto.response.InitiateUploadResponse;
import com.didan.stream.zdupload.dto.response.UploadStatusResponse;
import com.didan.stream.zdupload.model.UploadSession;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    
    /**
     * Initiate a new upload session
     * 
     * @param request the upload request details
     * @param userId the ID of the user initiating the upload (optional)
     * @return the upload session response with details needed by client
     */
    InitiateUploadResponse initiateUpload(InitiateUploadRequest request, String userId);
    
    /**
     * Upload a chunk of the file
     * 
     * @param uploadId the upload session ID
     * @param chunkNumber the chunk number (0-based index)
     * @param chunk the chunk data as MultipartFile
     * @param offset the byte offset in the complete file (for validation)
     * @return true if the chunk was successfully uploaded
     */
    boolean uploadChunk(String uploadId, int chunkNumber, MultipartFile chunk, long offset);
    
    /**
     * Complete the upload process
     * 
     * @param uploadId the upload session ID
     * @param request optional request data with parts info
     * @return the completion response with video ID and status
     */
    CompleteUploadResponse completeUpload(String uploadId, CompleteUploadRequest request);
    
    /**
     * Get the status of an upload session
     * 
     * @param uploadId the upload session ID
     * @return the current status of the upload session
     */
    UploadStatusResponse getUploadStatus(String uploadId);
    
    /**
     * Abort an ongoing upload session
     * 
     * @param uploadId the upload session ID
     */
    void abortUpload(String uploadId);
}
