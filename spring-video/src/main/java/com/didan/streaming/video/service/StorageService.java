package com.didan.streaming.video.service;

import com.didan.archetype.service.MinioService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final MinioClient minioClient;
    private final MinioService minioService;

    @Value("${app.minio.bucket.video}")
    private String videoBucket;

    @Value("${app.minio.bucket.hls}")
    private String hlsBucket;

    public String uploadVideo(MultipartFile file, UUID userId, UUID videoId) {
        try {
            String objectName = String.format("%s/%s/%s", userId, videoId, file.getOriginalFilename());
            minioService.createBucket(videoBucket);
            minioService.uploadFile(videoBucket, file, objectName, file.getContentType());
            return objectName;
        } catch (Exception e) {
            log.error("Lỗi khi upload video: {}", e.getMessage());
            throw new RuntimeException("Không thể upload video");
        }
    }

    public void uploadHls(String hlsPath, byte[] data) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(hlsPath)
                    .stream(new ByteArrayInputStream(data), data.length, -1)
                    .contentType("application/x-mpegURL")
                    .build());
        } catch (Exception e) {
            log.error("Lỗi khi upload HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể upload HLS");
        }
    }

    public byte[] getVideo(String objectName) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .build());
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Lỗi khi lấy video: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy video");
        }
    }

    public byte[] getHls(String objectName) {
        try {
            GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .build());
            return response.readAllBytes();
        } catch (Exception e) {
            log.error("Lỗi khi lấy HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy HLS");
        }
    }

    public List<String> listHlsFiles(String prefix) {
        try {
            List<String> result = new ArrayList<>();
            Iterable<Result<Item>> objects = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(hlsBucket)
                    .prefix(prefix)
                    .recursive(true)
                    .build());
            for (Result<Item> object : objects) {
                result.add(object.get().objectName());
            }
            return result;
        } catch (Exception e) {
            log.error("Lỗi khi liệt kê file HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể liệt kê file HLS");
        }
    }

    public void deleteVideo(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(videoBucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("Lỗi khi xóa video: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa video");
        }
    }

    public void deleteHls(String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            log.error("Lỗi khi xóa HLS: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa HLS");
        }
    }

    public String createLivestreamVideo(UUID userId, UUID streamId, String streamName) {
        try {
            // Create a folder structure for the livestream
            String objectName = String.format("%s/%s/livestream.m3u8", userId, streamId);

            // Create empty initial HLS playlist file
            String initialPlaylist = "#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:4\n#EXT-X-MEDIA-SEQUENCE:0\n";
            byte[] playlistData = initialPlaylist.getBytes();

            // Upload initial playlist to HLS bucket
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(playlistData), playlistData.length, -1)
                    .contentType("application/x-mpegURL")
                    .build());

            // Create metadata file with stream info
            String metadataObjectName = String.format("%s/%s/metadata.json", userId, streamId);
            String metadata = String.format("{\"streamName\":\"%s\",\"startTime\":\"%s\",\"status\":\"live\"}",
                                          streamName,
                                          java.time.Instant.now().toString());
            byte[] metadataBytes = metadata.getBytes();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(metadataObjectName)
                    .stream(new ByteArrayInputStream(metadataBytes), metadataBytes.length, -1)
                    .contentType("application/json")
                    .build());

            return objectName;
        } catch (Exception e) {
            log.error("Lỗi khi tạo video livestream: {}", e.getMessage());
            throw new RuntimeException("Không thể tạo video livestream");
        }
    }

    public void updateLivestreamSegment(UUID userId, UUID streamId, String segmentName, byte[] segmentData) {
        try {
            // Create path for the new segment
            String objectName = String.format("%s/%s/segments/%s", userId, streamId, segmentName);

            // Upload segment file
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(objectName)
                    .stream(new ByteArrayInputStream(segmentData), segmentData.length, -1)
                    .contentType("video/MP2T")
                    .build());

            // Update the main playlist to include the new segment
            updateLivestreamPlaylist(userId, streamId, segmentName);

        } catch (Exception e) {
            log.error("Lỗi khi cập nhật segment livestream: {}", e.getMessage());
            throw new RuntimeException("Không thể cập nhật segment livestream");
        }
    }

    private void updateLivestreamPlaylist(UUID userId, UUID streamId, String newSegmentName) throws Exception {
        // Get current playlist
        String playlistObjectName = String.format("%s/%s/livestream.m3u8", userId, streamId);
        byte[] currentPlaylistBytes = getHls(playlistObjectName);
        String currentPlaylist = new String(currentPlaylistBytes);

        // Add new segment to playlist
        StringBuilder updatedPlaylist = new StringBuilder(currentPlaylist);
        if (!currentPlaylist.contains(newSegmentName)) {
            updatedPlaylist.append("#EXTINF:4.0,\n");
            updatedPlaylist.append("segments/").append(newSegmentName).append("\n");
        }

        // Upload updated playlist
        byte[] updatedPlaylistBytes = updatedPlaylist.toString().getBytes();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(hlsBucket)
                .object(playlistObjectName)
                .stream(new ByteArrayInputStream(updatedPlaylistBytes), updatedPlaylistBytes.length, -1)
                .contentType("application/x-mpegURL")
                .build());
    }

    public void endLivestream(UUID userId, UUID streamId) {
        try {
            // Update metadata to show the stream has ended
            String metadataObjectName = String.format("%s/%s/metadata.json", userId, streamId);

            // Get current metadata
            byte[] metadataBytes = getHls(metadataObjectName);
            String metadata = new String(metadataBytes);

            // Update status to "ended" and add end time
            metadata = metadata.replace("\"status\":\"live\"",
                       String.format("\"status\":\"ended\",\"endTime\":\"%s\"",
                       java.time.Instant.now().toString()));

            // Upload updated metadata
            byte[] updatedMetadataBytes = metadata.getBytes();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(hlsBucket)
                    .object(metadataObjectName)
                    .stream(new ByteArrayInputStream(updatedMetadataBytes), updatedMetadataBytes.length, -1)
                    .contentType("application/json")
                    .build());

            // Finalize the playlist by adding the end marker
            String playlistObjectName = String.format("%s/%s/livestream.m3u8", userId, streamId);
            byte[] playlistBytes = getHls(playlistObjectName);
            String playlist = new String(playlistBytes);

            if (!playlist.contains("#EXT-X-ENDLIST")) {
                playlist += "#EXT-X-ENDLIST\n";
                byte[] updatedPlaylistBytes = playlist.getBytes();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(hlsBucket)
                        .object(playlistObjectName)
                        .stream(new ByteArrayInputStream(updatedPlaylistBytes), updatedPlaylistBytes.length, -1)
                        .contentType("application/x-mpegURL")
                        .build());
            }
        } catch (Exception e) {
            log.error("Lỗi khi kết thúc livestream: {}", e.getMessage());
            throw new RuntimeException("Không thể kết thúc livestream");
        }
    }
}

