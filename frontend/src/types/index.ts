export interface User {
  id: number;
  username: string;
  email: string;
  avatar?: string;
  createdAt: string;
}

export interface Video {
  id: number;
  title: string;
  description: string;
  url: string;
  thumbnailUrl: string;
  userId: number;
  user?: User;
  status: "PROCESSING" | "READY" | "ERROR";
  createdAt: string;
  updatedAt: string;
}

export interface LiveStream {
  id: number;
  title: string;
  description: string;
  streamKey: string;
  status: "LIVE" | "ENDED";
  userId: number;
  user?: User;
  viewerCount: number;
  startedAt?: string;
  endedAt?: string;
}

export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}
