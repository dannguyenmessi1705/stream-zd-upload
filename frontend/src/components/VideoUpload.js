import React, { useState } from 'react';
import { Upload, Form, Input, Button, message } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { uploadVideo } from '../services/api';

const VideoUpload = () => {
  const [form] = Form.useForm();
  const [fileList, setFileList] = useState([]);
  const [uploading, setUploading] = useState(false);

  const handleUpload = async (values) => {
    if (fileList.length === 0) {
      message.error('Vui lòng chọn video để upload');
      return;
    }

    const formData = new FormData();
    formData.append('file', fileList[0].originFileObj);
    formData.append('request', JSON.stringify({
      title: values.title,
      description: values.description
    }));

    setUploading(true);
    try {
      await uploadVideo(formData);
      message.success('Upload video thành công');
      form.resetFields();
      setFileList([]);
    } catch (error) {
      message.error('Upload video thất bại: ' + error.message);
    } finally {
      setUploading(false);
    }
  };

  const uploadProps = {
    beforeUpload: (file) => {
      const isVideo = file.type.startsWith('video/');
      if (!isVideo) {
        message.error('Chỉ hỗ trợ upload file video!');
        return false;
      }
      return false;
    },
    fileList,
    onChange: ({ fileList }) => setFileList(fileList),
    maxCount: 1
  };

  return (
    <div style={{ maxWidth: 600, margin: '0 auto', padding: 24 }}>
      <h2>Upload Video</h2>
      <Form form={form} onFinish={handleUpload} layout="vertical">
        <Form.Item
          name="title"
          label="Tiêu đề"
          rules={[{ required: true, message: 'Vui lòng nhập tiêu đề' }]}
        >
          <Input placeholder="Nhập tiêu đề video" />
        </Form.Item>

        <Form.Item
          name="description"
          label="Mô tả"
        >
          <Input.TextArea placeholder="Nhập mô tả video" rows={4} />
        </Form.Item>

        <Form.Item label="Video">
          <Upload {...uploadProps}>
            <Button icon={<UploadOutlined />}>Chọn Video</Button>
          </Upload>
        </Form.Item>

        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            loading={uploading}
            disabled={fileList.length === 0}
          >
            {uploading ? 'Đang upload...' : 'Upload'}
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default VideoUpload; 