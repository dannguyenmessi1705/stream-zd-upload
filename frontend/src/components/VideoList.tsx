import React, { useEffect, useState } from "react";
import { List, Card, Avatar, Space, Tag } from "antd";
import { UserOutlined, ClockCircleOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import { getVideos } from "../services/api";
import { Video } from "../types";
import moment from "moment";

const VideoList: React.FC = () => {
  const [videos, setVideos] = useState<Video[]>([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [total, setTotal] = useState(0);

  const fetchVideos = async (pageNumber: number) => {
    try {
      setLoading(true);
      const response = await getVideos(pageNumber);
      setVideos(response.data);
      setTotal(response.data.length);
    } catch (error) {
      console.error("Failed to fetch videos:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVideos(page);
  }, [page]);

  const getStatusColor = (status: string) => {
    switch (status) {
      case "READY":
        return "success";
      case "PROCESSING":
        return "processing";
      case "ERROR":
        return "error";
      default:
        return "default";
    }
  };

  return (
    <List
      grid={{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 3, xl: 4, xxl: 4 }}
      dataSource={videos}
      loading={loading}
      pagination={{
        onChange: (page) => setPage(page - 1),
        total: total,
        pageSize: 12,
        current: page + 1,
      }}
      renderItem={(video) => (
        <List.Item>
          <Link to={`/video/${video.id}`}>
            <Card
              hoverable
              cover={
                <img
                  alt={video.title}
                  src={video.thumbnailUrl}
                  style={{ height: 200, objectFit: "cover" }}
                />
              }
            >
              <Card.Meta
                title={video.title}
                description={
                  <Space direction="vertical" size={2}>
                    <Space>
                      <Avatar
                        size="small"
                        icon={<UserOutlined />}
                        src={video.user?.avatar}
                      />
                      <span>{video.user?.username}</span>
                    </Space>
                    <Space>
                      <ClockCircleOutlined />
                      <span>{moment(video.createdAt).fromNow()}</span>
                    </Space>
                    <Tag color={getStatusColor(video.status)}>
                      {video.status}
                    </Tag>
                  </Space>
                }
              />
            </Card>
          </Link>
        </List.Item>
      )}
    />
  );
};

export default VideoList;
