import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
});

// Thêm interceptor để xử lý token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth API
export const login = (username, password) =>
  api.post('/auth/login', { username, password });

export const register = (username, password, email, fullName) =>
  api.post('/auth/register', { username, password, email, fullName });

// Video API
export const uploadVideo = (formData) =>
  api.post('/videos', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });

export const getVideos = (page = 0, size = 10) =>
  api.get(`/videos?page=${page}&size=${size}`);

export const getVideo = (id) => api.get(`/videos/${id}`);

export const deleteVideo = (id) => api.delete(`/videos/${id}`);

// Livestream API
export const createLivestream = (title, description) =>
  api.post('/livestreams', { title, description });

export const getLivestreams = (page = 0, size = 10) =>
  api.get(`/livestreams?page=${page}&size=${size}`);

export const getLivestream = (id) => api.get(`/livestreams/${id}`);

export const startLivestream = (id) => api.post(`/livestreams/${id}/start`);

export const endLivestream = (id) => api.post(`/livestreams/${id}/end`);

export const deleteLivestream = (id) => api.delete(`/livestreams/${id}`); 