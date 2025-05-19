import React, { useState, useEffect } from "react";
import { List, Card, Button, Modal, message } from "antd";
import { DeleteOutlined, PlayCircleOutlined } from "@ant-design/icons";
import { getVideos, deleteVideo } from "../services/api";
import ReactPlayer from "react-player";
import moment from "moment";

const VideoList = () => {
  const [videos, setVideos] = useState([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [selectedVideo, setSelectedVideo] = useState(null);
  const [showPlayer, setShowPlayer] = useState(false);

  const fetchVideos = async () => {
    setLoading(true);
    try {
      const response = await getVideos(page);
      setVideos(response.data.content);
      setTotal(response.data.totalElements);
    } catch (error) {
      message.error("Lấy danh sách video thất bại: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchVideos();
  }, [page]);

  const handleDelete = async (id) => {
    try {
      await deleteVideo(id);
      message.success("Xóa video thành công");
      fetchVideos();
    } catch (error) {
      message.error("Xóa video thất bại: " + error.message);
    }
  };

  const handlePlay = (video) => {
    setSelectedVideo(video);
    setShowPlayer(true);
  };

  return (
    <div style={{ padding: 24 }}>
      <h2>Danh Sách Video</h2>

      <List
        grid={{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 3, xl: 4, xxl: 4 }}
        dataSource={videos}
        loading={loading}
        pagination={{
          total,
          pageSize: 12,
          current: page + 1,
          onChange: (p) => setPage(p - 1),
        }}
        renderItem={(video) => (
          <List.Item>
            <Card
              cover={
                <div
                  style={{
                    height: 200,
                    background: "#f0f0f0",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                  }}
                >
                  <PlayCircleOutlined
                    style={{ fontSize: 48, cursor: "pointer" }}
                    onClick={() => handlePlay(video)}
                  />
                </div>
              }
              actions={[
                <Button
                  type="text"
                  danger
                  icon={<DeleteOutlined />}
                  onClick={() => handleDelete(video.id)}
                >
                  Xóa
                </Button>,
              ]}
            >
              <Card.Meta
                title={video.title}
                description={
                  <>
                    <p>{video.description}</p>
                    <p>Đăng tải: {moment(video.createdAt).fromNow()}</p>
                  </>
                }
              />
            </Card>
          </List.Item>
        )}
      />

      <Modal
        title={selectedVideo?.title}
        open={showPlayer}
        onCancel={() => setShowPlayer(false)}
        footer={null}
        width={800}
      >
        {selectedVideo && (
          <ReactPlayer
            url={selectedVideo.hlsUrl}
            controls
            width="100%"
            height="450px"
          />
        )}
      </Modal>
    </div>
  );
};

export default VideoList;
