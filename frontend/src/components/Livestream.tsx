import React, { useState, useEffect } from "react";
import { Card, Button, Input, Form, message, Space, Typography } from "antd";
import { createLiveStream, getLiveStreamById } from "../services/api";
import { LiveStream as LiveStreamType } from "../types";
import ReactPlayer from "react-player";

const { Title, Text } = Typography;

const Livestream: React.FC = () => {
  const [form] = Form.useForm();
  const [livestream, setLivestream] = useState<LiveStreamType | null>(null);
  const [loading, setLoading] = useState(false);

  const handleCreateStream = async (values: {
    title: string;
    description: string;
  }) => {
    try {
      setLoading(true);
      const response = await createLiveStream(values.title, values.description);
      setLivestream(response.data);
      message.success("Livestream created successfully");
    } catch (error) {
      console.error("Failed to create livestream:", error);
      message.error("Failed to create livestream");
    } finally {
      setLoading(false);
    }
  };

  const copyStreamKey = () => {
    if (livestream?.streamKey) {
      navigator.clipboard.writeText(livestream.streamKey);
      message.success("Stream key copied to clipboard");
    }
  };

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: "20px" }}>
      {!livestream ? (
        <>
          <Title level={2}>Create New Livestream</Title>
          <Form form={form} layout="vertical" onFinish={handleCreateStream}>
            <Form.Item
              name="title"
              label="Title"
              rules={[
                { required: true, message: "Please input stream title!" },
              ]}
            >
              <Input placeholder="Enter stream title" />
            </Form.Item>

            <Form.Item
              name="description"
              label="Description"
              rules={[
                { required: true, message: "Please input stream description!" },
              ]}
            >
              <Input.TextArea rows={4} placeholder="Enter stream description" />
            </Form.Item>

            <Form.Item>
              <Button type="primary" htmlType="submit" loading={loading}>
                Create Stream
              </Button>
            </Form.Item>
          </Form>
        </>
      ) : (
        <Card title={livestream.title}>
          <Space direction="vertical" size="large" style={{ width: "100%" }}>
            <div>
              <Text strong>Stream Key: </Text>
              <Text type="secondary" style={{ fontFamily: "monospace" }}>
                {livestream.streamKey.substring(0, 8)}...
              </Text>
              <Button type="link" onClick={copyStreamKey}>
                Copy
              </Button>
            </div>

            <div>
              <Text strong>RTMP URL: </Text>
              <Text>rtmp://localhost:1935/live</Text>
            </div>

            {livestream.status === "LIVE" && (
              <div style={{ width: "100%", aspectRatio: "16/9" }}>
                <ReactPlayer
                  url={`http://localhost:8080/live/${livestream.streamKey}/index.m3u8`}
                  controls
                  width="100%"
                  height="100%"
                  playing
                />
              </div>
            )}

            <div>
              <Text strong>Status: </Text>
              <Text>{livestream.status}</Text>
            </div>

            <div>
              <Text strong>Viewers: </Text>
              <Text>{livestream.viewerCount}</Text>
            </div>
          </Space>
        </Card>
      )}
    </div>
  );
};

export default Livestream;
