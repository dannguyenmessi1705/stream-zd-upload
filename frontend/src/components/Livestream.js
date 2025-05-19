import React, { useState } from "react";
import { Form, Input, Button, Card, message } from "antd";
import {
  createLivestream,
  startLivestream,
  endLivestream,
} from "../services/api";
import ReactPlayer from "react-player";

const Livestream = () => {
  const [form] = Form.useForm();
  const [livestream, setLivestream] = useState(null);
  const [isLive, setIsLive] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleCreate = async (values) => {
    setLoading(true);
    try {
      const response = await createLivestream(values.title, values.description);
      setLivestream(response.data);
      message.success("Tạo phiên livestream thành công");
      form.resetFields();
    } catch (error) {
      message.error("Tạo phiên livestream thất bại: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleStart = async () => {
    if (!livestream) return;

    setLoading(true);
    try {
      await startLivestream(livestream.id);
      setIsLive(true);
      message.success("Bắt đầu livestream thành công");
    } catch (error) {
      message.error("Bắt đầu livestream thất bại: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  const handleEnd = async () => {
    if (!livestream) return;

    setLoading(true);
    try {
      await endLivestream(livestream.id);
      setIsLive(false);
      setLivestream(null);
      message.success("Kết thúc livestream thành công");
    } catch (error) {
      message.error("Kết thúc livestream thất bại: " + error.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: 24 }}>
      <h2>Livestream</h2>

      {!livestream ? (
        <Form form={form} onFinish={handleCreate} layout="vertical">
          <Form.Item
            name="title"
            label="Tiêu đề"
            rules={[{ required: true, message: "Vui lòng nhập tiêu đề" }]}
          >
            <Input placeholder="Nhập tiêu đề livestream" />
          </Form.Item>

          <Form.Item name="description" label="Mô tả">
            <Input.TextArea placeholder="Nhập mô tả livestream" rows={4} />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              Tạo Phiên Livestream
            </Button>
          </Form.Item>
        </Form>
      ) : (
        <Card title={livestream.title}>
          <p>{livestream.description}</p>

          {isLive && livestream.hlsUrl && (
            <div style={{ marginBottom: 16 }}>
              <ReactPlayer
                url={livestream.hlsUrl}
                controls
                width="100%"
                height="400px"
                playing
              />
            </div>
          )}

          <div style={{ marginTop: 16 }}>
            {!isLive ? (
              <div>
                <p>RTMP URL: {livestream.rtmpUrl}</p>
                <p>Stream Key: {livestream.streamKey}</p>
                <Button type="primary" onClick={handleStart} loading={loading}>
                  Bắt Đầu Livestream
                </Button>
              </div>
            ) : (
              <Button
                type="primary"
                danger
                onClick={handleEnd}
                loading={loading}
              >
                Kết Thúc Livestream
              </Button>
            )}
          </div>
        </Card>
      )}
    </div>
  );
};

export default Livestream;
