import React, { useState } from "react";
import { Upload, Button, Form, Input, Progress } from "antd";
import { UploadOutlined } from "@ant-design/icons";
import { videoService } from "../services/videoService";
import { CreateVideoRequest, VideoStatus } from "../types/video";
import { useNavigate } from "react-router-dom";
import { useMessage } from "../hooks/useMessage";

const VideoUpload: React.FC = () => {
  const [uploading, setUploading] = useState(false);
  const [progress, setProgress] = useState(0);
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const { message } = useMessage();

  const onFinish = async (values: any) => {
    const file = values.file?.file;
    if (!file) {
      message.error("Vui lòng chọn file video");
      return;
    }

    const request: CreateVideoRequest = {
      title: values.title,
      description: values.description,
      file: file.originFileObj,
    };

    try {
      setUploading(true);
      const video = await videoService.uploadVideo(request, (progress) => {
        setProgress(progress);
      });
      message.success("Tải lên video thành công");
      navigate(`/videos/${video.id}`);
    } catch (error) {
      message.error("Không thể tải lên video");
    } finally {
      setUploading(false);
      setProgress(0);
    }
  };

  const normFile = (e: any) => {
    if (Array.isArray(e)) {
      return e;
    }
    return e?.fileList;
  };

  return (
    <div style={{ maxWidth: 600, margin: "0 auto", padding: "20px" }}>
      <h2>Upload Video</h2>
      <Form
        form={form}
        layout="vertical"
        onFinish={onFinish}
        disabled={uploading}
      >
        <Form.Item
          name="title"
          label="Title"
          rules={[{ required: true, message: "Please input video title!" }]}
        >
          <Input placeholder="Enter video title" />
        </Form.Item>

        <Form.Item
          name="description"
          label="Description"
          rules={[
            { required: true, message: "Please input video description!" },
          ]}
        >
          <Input.TextArea rows={4} placeholder="Enter video description" />
        </Form.Item>

        <Form.Item
          name="file"
          label="Video File"
          valuePropName="fileList"
          getValueFromEvent={normFile}
          rules={[{ required: true, message: "Please select a video file!" }]}
        >
          <Upload.Dragger
            name="file"
            beforeUpload={() => false}
            maxCount={1}
            accept="video/*"
          >
            <p className="ant-upload-drag-icon">
              <UploadOutlined />
            </p>
            <p className="ant-upload-text">
              Click or drag video file to this area to upload
            </p>
          </Upload.Dragger>
        </Form.Item>

        {uploading && (
          <Progress
            percent={Math.round(progress)}
            status={progress === 100 ? "success" : "active"}
            style={{ marginBottom: 16 }}
          />
        )}

        <Form.Item>
          <Button type="primary" htmlType="submit" loading={uploading}>
            {uploading ? "Uploading..." : "Upload"}
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default VideoUpload;
