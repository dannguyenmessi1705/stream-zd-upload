import axios from "axios";
import { ApiResponse, User, Video, LiveStream } from "../types";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Add token to request header
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Video APIs
export const uploadVideo = async (
  file: File,
  title: string,
  description: string
): Promise<ApiResponse<Video>> => {
  const formData = new FormData();
  formData.append("file", file);
  formData.append("title", title);
  formData.append("description", description);

  const response = await api.post<ApiResponse<Video>>(
    "/videos/upload",
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    }
  );
  return response.data;
};

export const getVideos = async (
  page = 0,
  size = 10
): Promise<ApiResponse<Video[]>> => {
  const response = await api.get<ApiResponse<Video[]>>(
    `/videos?page=${page}&size=${size}`
  );
  return response.data;
};

export const getVideoById = async (id: number): Promise<ApiResponse<Video>> => {
  const response = await api.get<ApiResponse<Video>>(`/videos/${id}`);
  return response.data;
};

// LiveStream APIs
export const createLiveStream = async (
  title: string,
  description: string
): Promise<ApiResponse<LiveStream>> => {
  const response = await api.post<ApiResponse<LiveStream>>("/livestreams", {
    title,
    description,
  });
  return response.data;
};

export const getLiveStreams = async (
  page = 0,
  size = 10
): Promise<ApiResponse<LiveStream[]>> => {
  const response = await api.get<ApiResponse<LiveStream[]>>(
    `/livestreams?page=${page}&size=${size}`
  );
  return response.data;
};

export const getLiveStreamById = async (
  id: number
): Promise<ApiResponse<LiveStream>> => {
  const response = await api.get<ApiResponse<LiveStream>>(`/livestreams/${id}`);
  return response.data;
};

// Auth APIs
export const login = async (
  username: string,
  password: string
): Promise<ApiResponse<{ token: string; user: User }>> => {
  const response = await api.post<ApiResponse<{ token: string; user: User }>>(
    "/auth/login",
    { username, password }
  );
  return response.data;
};

export const register = async (
  username: string,
  email: string,
  password: string
): Promise<ApiResponse<User>> => {
  const response = await api.post<ApiResponse<User>>("/auth/register", {
    username,
    email,
    password,
  });
  return response.data;
};

export const getCurrentUser = async (): Promise<ApiResponse<User>> => {
  const response = await api.get<ApiResponse<User>>("/auth/me");
  return response.data;
};
