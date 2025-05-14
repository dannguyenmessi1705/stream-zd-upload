package com.didan.stream.zdupload.repository;

import com.didan.stream.zdupload.model.UploadSession;

import java.util.Optional;

public interface UploadSessionRepository {
    
    /**
     * Save upload session
     * 
     * @param session the upload session to save
     * @return the saved upload session
     */
    UploadSession save(UploadSession session);
    
    /**
     * Find an upload session by ID
     * 
     * @param uploadId the upload session ID
     * @return an Optional containing the upload session if found
     */
    Optional<UploadSession> findById(String uploadId);
    
    /**
     * Delete an upload session by ID
     * 
     * @param uploadId the upload session ID
     */
    void deleteById(String uploadId);
    
    /**
     * Update the chunk status for an upload session
     * 
     * @param uploadId the upload session ID
     * @param chunkNumber the chunk number
     * @param uploaded whether the chunk is uploaded successfully
     * @return the updated upload session
     */
    UploadSession updateChunkStatus(String uploadId, int chunkNumber, boolean uploaded);
}
