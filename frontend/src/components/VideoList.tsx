import React, { useEffect, useState, useCallback } from "react";
import { List, Card, Avatar, Space, Tag, Table, Button } from "antd";
import {
  UserOutlined,
  ClockCircleOutlined,
  DeleteOutlined,
  PlayCircleOutlined,
} from "@ant-design/icons";
import { Link } from "react-router-dom";
import { getVideos } from "../services/api";
import { Video } from "../types";
import moment from "moment";
import { videoService } from "../services/videoService";
import { useMessage } from "../hooks/useMessage";

const VideoList: React.FC = () => {
  const [videos, setVideos] = useState<Video[]>([]);
  const [loading, setLoading] = useState(false);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(1);
  const [error, setError] = useState<string | null>(null);
  const pageSize = 10;
  const { message } = useMessage();

  const fetchVideos = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await videoService.getVideos(page - 1, pageSize);
      setVideos(response.content);
      setTotal(response.totalElements);
    } catch (error) {
      setError("Không thể tải danh sách video");
    } finally {
      setLoading(false);
    }
  }, [page]);

  useEffect(() => {
    fetchVideos();
  }, [fetchVideos]);

  useEffect(() => {
    if (error) {
      message.error(error);
    }
  }, [error, message]);

  const handleDelete = useCallback(
    async (id: string) => {
      try {
        await videoService.deleteVideo(id);
        message.success("Xóa video thành công");
        fetchVideos();
      } catch (error) {
        message.error("Không thể xóa video");
      }
    },
    [fetchVideos, message]
  );

  const columns = [
    {
      title: "Tiêu đề",
      dataIndex: "title",
      key: "title",
      render: (text: string, record: Video) => (
        <Link to={`/videos/${record.id}`}>{text}</Link>
      ),
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (status: string) => (
        <Tag color={videoService.getStatusColor(status)}>
          {videoService.getStatusText(status)}
        </Tag>
      ),
    },
    {
      title: "Kích thước",
      dataIndex: "fileSize",
      key: "fileSize",
      render: (size: number) => `${(size / 1024 / 1024).toFixed(2)} MB`,
    },
    {
      title: "Người tạo",
      dataIndex: "owner",
      key: "owner",
      render: (owner: any) => owner.fullName,
    },
    {
      title: "Thao tác",
      key: "action",
      render: (_: any, record: Video) => (
        <Space size="middle">
          <Button
            type="link"
            icon={<PlayCircleOutlined />}
            disabled={!videoService.getHlsUrl(record)}
            href={`/videos/${record.id}`}
          >
            Xem
          </Button>
          <Button
            type="link"
            danger
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(record.id)}
          >
            Xóa
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <Table
      columns={columns}
      dataSource={videos}
      rowKey="id"
      loading={loading}
      pagination={{
        current: page,
        pageSize,
        total,
        onChange: setPage,
      }}
    />
  );
};

export default VideoList;
