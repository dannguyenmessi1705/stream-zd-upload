import axios from "axios";
import { CreateVideoRequest, Video, VideoStatus } from "../types/video";

// Sử dụng import.meta.env thay vì process.env trong Vite
const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

export const videoService = {
  async getVideos(
    page = 0,
    size = 10
  ): Promise<{ content: Video[]; totalElements: number }> {
    const response = await axios.get(
      `${API_URL}/videos?page=${page}&size=${size}`
    );
    return response.data;
  },

  async getVideo(id: string): Promise<Video> {
    const response = await axios.get(`${API_URL}/videos/${id}`);
    return response.data;
  },

  async uploadVideo(
    request: CreateVideoRequest,
    onProgress?: (progress: number) => void
  ): Promise<Video> {
    const formData = new FormData();
    formData.append("title", request.title);
    if (request.description) {
      formData.append("description", request.description);
    }
    formData.append("file", request.file);

    const response = await axios.post(`${API_URL}/videos`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress && progressEvent.total) {
          const progress = (progressEvent.loaded * 100) / progressEvent.total;
          onProgress(progress);
        }
      },
    });

    return response.data;
  },

  async deleteVideo(id: string): Promise<void> {
    await axios.delete(`${API_URL}/videos/${id}`);
  },

  getHlsUrl(video: Video): string | null {
    if (video.status !== VideoStatus.READY || !video.hlsPath) {
      return null;
    }
    return `${API_URL}/videos/hls/${video.hlsPath}/playlist.m3u8`;
  },

  getStatusColor(status: VideoStatus): string {
    switch (status) {
      case VideoStatus.UPLOADING:
        return "blue";
      case VideoStatus.UPLOADED:
        return "cyan";
      case VideoStatus.PROCESSING:
        return "orange";
      case VideoStatus.READY:
        return "green";
      case VideoStatus.ERROR:
        return "red";
      default:
        return "gray";
    }
  },

  getStatusText(status: VideoStatus): string {
    switch (status) {
      case VideoStatus.UPLOADING:
        return "Đang tải lên";
      case VideoStatus.UPLOADED:
        return "Đã tải lên";
      case VideoStatus.PROCESSING:
        return "Đang xử lý";
      case VideoStatus.READY:
        return "Sẵn sàng";
      case VideoStatus.ERROR:
        return "Lỗi";
      default:
        return "Không xác định";
    }
  },
};
