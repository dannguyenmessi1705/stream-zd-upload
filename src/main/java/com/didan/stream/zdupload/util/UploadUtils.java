package com.didan.stream.zdupload.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class for handling resumable uploads
 */
@Slf4j
public class UploadUtils {
    
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    
    /**
     * Calculate MD5 hash of a byte array
     * 
     * @param bytes the input bytes
     * @return MD5 hash as hex string
     */
    public static String calculateMD5(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(bytes);
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("Failed to calculate MD5: {}", e.getMessage(), e);
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }
    
    /**
     * Convert bytes to hexadecimal string
     * 
     * @param bytes the input bytes
     * @return hex string representation
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    /**
     * Calculate the number of chunks for a given file size and chunk size
     * 
     * @param fileSize total file size in bytes
     * @param chunkSize chunk size in bytes
     * @return number of chunks
     */
    public static int calculateTotalChunks(long fileSize, int chunkSize) {
        return (int) Math.ceil((double) fileSize / chunkSize);
    }
    
    /**
     * Generate a safe object path based on video ID and filename
     * 
     * @param videoId unique video identifier
     * @param fileName original filename
     * @return safe storage path
     */
    public static String generateObjectPath(String videoId, String fileName) {
        // Remove potentially problematic characters from filename
        String safeName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
        return String.format("videos/%s/%s", videoId, safeName);
    }
}
