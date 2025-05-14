# Upload Service for Video Streaming Platform

A microservice responsible for handling video file uploads via resumable upload protocol, part of a video streaming platform backend architecture.

## Features

- **Resumable Upload**: Supports upload of large video files with ability to resume after interruptions
- **Chunk-Based Upload**: Files are uploaded in chunks for better reliability and performance
- **File Validation**: Validates file types and sizes before processing
- **Integration with Object Storage**: Uses MinIO (S3-compatible storage) for file storage
- **Kafka Integration**: Notifies other services when uploads are completed
- **Upload Session Management**: Tracks upload sessions using Redis
- **RESTful API**: Clean API design following REST principles

## API Documentation

### Initiate Upload

Starts a new upload session.

```
POST /v1/uploads/initiate
```

Request Body:
```json
{
  "fileName": "example-video.mp4",
  "fileSize": 104857600,
  "contentType": "video/mp4",
  "metadata": {
    "title": "My Video",
    "description": "A sample video",
    "tags": ["sample", "demo"]
  }
}
```

Response:
```json
{
  "uploadId": "unique-upload-id",
  "chunkSize": 8388608,
  "totalChunks": 13
}
```

### Upload Chunk

Uploads a chunk of the file.

```
PUT /v1/uploads/{uploadId}/chunk?chunkNumber={chunkNumber}&offset={offset}
```

- `uploadId`: The unique upload session ID
- `chunkNumber`: Zero-based index of the chunk
- `offset`: Byte offset in the complete file

Request: Multi-part file upload with the chunk data.

### Complete Upload

Finalizes the upload after all chunks have been uploaded.

```
POST /v1/uploads/{uploadId}/complete
```

Response:
```json
{
  "videoId": "new-video-id",
  "status": "pending_transcode",
  "objectPath": "videos/new-video-id/example-video.mp4"
}
```

### Check Upload Status

Get the current status of an upload session.

```
GET /v1/uploads/{uploadId}/status
```

Response:
```json
{
  "uploadId": "unique-upload-id",
  "fileName": "example-video.mp4",
  "fileSize": 104857600,
  "uploadedBytes": 52428800,
  "progressPercentage": 50.0,
  "totalChunks": 13,
  "uploadedChunks": 6,
  "status": "in_progress",
  "expiresAt": "2025-05-15T10:30:00"
}
```

### Abort Upload

Cancels an upload and cleans up resources.

```
DELETE /v1/uploads/{uploadId}
```

## Configuration

Key application properties:

```properties
# Server configuration
server.port=8080

# MinIO configuration
minio.endpoint=http://localhost:9000
minio.accessKey=minioadmin
minio.secretKey=minioadmin
minio.bucket.name=video-upload
minio.temp.bucket.name=video-upload-temp

# Upload configuration
upload.chunk-size=8MB
upload.expiry-time=24

# Kafka configuration
spring.kafka.bootstrap-servers=localhost:9092
kafka.topic.video-uploaded=video-file-uploaded

# Redis configuration
spring.redis.host=localhost
spring.redis.port=6379
```

## Event Publishing

When an upload is completed, the service publishes a `VideoUploadedEvent` to Kafka with the following information:

```json
{
  "videoId": "unique-video-id",
  "objectPath": "videos/video-id/filename.mp4",
  "fileName": "filename.mp4",
  "contentType": "video/mp4",
  "fileSize": 104857600,
  "userId": "user-123",
  "uploadedAt": "2025-05-14T09:30:00",
  "checksum": "md5-hash-value",
  "metadata": {
    "title": "Video Title",
    "description": "Video Description",
    "tags": ["tag1", "tag2"]
  }
}
```

This event is consumed by the Transcoding Service to start the video processing pipeline.

## Dependencies

- Spring Boot 3.4.4
- MinIO Client 8.5.8
- Spring Kafka
- Spring Data Redis
- Commons IO 2.15.1

## Running the Service

1. Start MinIO, Kafka, and Redis (using Docker or local installations)
2. Configure application properties
3. Run the application:
   ```
   ./mvnw spring-boot:run
   ```

## Security Considerations

- The service includes CORS configuration
- In production, implement proper authentication/authorization (JWT, OAuth2)
- Consider implementing rate limiting for upload endpoints
- Add virus scanning for uploaded files before processing
