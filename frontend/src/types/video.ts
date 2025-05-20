export enum VideoStatus {
  UPLOADING = "UPLOADING",
  UPLOADED = "UPLOADED",
  PROCESSING = "PROCESSING",
  READY = "READY",
  ERROR = "ERROR",
}

export interface Video {
  id: string;
  title: string;
  description?: string;
  status: VideoStatus;
  duration?: number;
  originalFilename: string;
  minioPath?: string;
  hlsPath?: string;
  fileSize: number;
  createdAt: string;
  updatedAt: string;
  owner: {
    id: string;
    username: string;
    fullName: string;
  };
}

export interface CreateVideoRequest {
  title: string;
  description?: string;
  file: File;
}

export interface VideoUploadProgress {
  videoId: string;
  progress: number;
  status: VideoStatus;
}
