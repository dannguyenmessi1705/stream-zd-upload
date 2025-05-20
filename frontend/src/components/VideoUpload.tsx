import React, { useState } from "react";
import { Upload, Button, Form, Input, message } from "antd";
import { UploadOutlined } from "@ant-design/icons";
import { uploadVideo } from "../services/api";
import type { UploadFile } from "antd/es/upload/interface";

const VideoUpload: React.FC = () => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState<UploadFile[]>([]);
  const [uploading, setUploading] = useState(false);

  const handleUpload = async () => {
    try {
      const values = await form.validateFields();
      if (fileList.length === 0) {
        message.error("Please select a video file");
        return;
      }

      setUploading(true);
      const file = fileList[0].originFileObj as File;
      const response = await uploadVideo(
        file,
        values.title,
        values.description
      );

      message.success("Video uploaded successfully");
      form.resetFields();
      setFileList([]);
    } catch (error) {
      message.error("Failed to upload video");
      console.error(error);
    } finally {
      setUploading(false);
    }
  };

  const uploadProps = {
    onRemove: () => {
      setFileList([]);
    },
    beforeUpload: (file: UploadFile) => {
      setFileList([file]);
      return false;
    },
    fileList,
    maxCount: 1,
    accept: "video/*",
  };

  return (
    <div style={{ maxWidth: 600, margin: "0 auto", padding: "20px" }}>
      <h2>Upload Video</h2>
      <Form form={form} layout="vertical">
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

        <Form.Item label="Video File">
          <Upload {...uploadProps}>
            <Button icon={<UploadOutlined />}>Select Video</Button>
          </Upload>
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            onClick={handleUpload}
            loading={uploading}
            disabled={fileList.length === 0}
          >
            {uploading ? "Uploading" : "Upload"}
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default VideoUpload;
