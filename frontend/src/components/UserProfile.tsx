import React, { useState, useEffect } from "react";
import {
  Card,
  Tabs,
  List,
  Avatar,
  Tag,
  Upload,
  Button,
  Form,
  Input,
  message,
} from "antd";
import { UserOutlined, UploadOutlined } from "@ant-design/icons";
import { getCurrentUser } from "../services/api";
import type { User, Video, LiveStream } from "../types";
import type { UploadFile } from "antd/es/upload/interface";
import moment from "moment";

const { TabPane } = Tabs;

const UserProfile: React.FC = () => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [avatarFile, setAvatarFile] = useState<UploadFile[]>([]);
  const [form] = Form.useForm();

  useEffect(() => {
    fetchUserData();
  }, []);

  const fetchUserData = async () => {
    try {
      setLoading(true);
      const response = await getCurrentUser();
      setUser(response.data);
      form.setFieldsValue({
        username: response.data.username,
        email: response.data.email,
      });
    } catch (error) {
      console.error("Failed to fetch user data:", error);
      message.error("Không thể tải thông tin người dùng");
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateProfile = async (values: any) => {
    try {
      // TODO: Implement update profile API
      message.success("Cập nhật thông tin thành công");
    } catch (error) {
      message.error("Cập nhật thông tin thất bại");
    }
  };

  const uploadProps = {
    onRemove: () => {
      setAvatarFile([]);
    },
    beforeUpload: (file: UploadFile) => {
      setAvatarFile([file]);
      return false;
    },
    fileList: avatarFile,
    maxCount: 1,
    accept: "image/*",
  };

  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: "20px" }}>
      <Card loading={loading}>
        <div style={{ textAlign: "center", marginBottom: 24 }}>
          <Avatar
            size={100}
            icon={<UserOutlined />}
            src={user?.avatar}
            style={{ marginBottom: 16 }}
          />
          <h2>{user?.username}</h2>
          <p>Tham gia từ: {moment(user?.createdAt).format("DD/MM/YYYY")}</p>
        </div>

        <Tabs defaultActiveKey="1">
          <TabPane tab="Thông tin cá nhân" key="1">
            <Form form={form} layout="vertical" onFinish={handleUpdateProfile}>
              <Form.Item name="avatar" label="Avatar">
                <Upload {...uploadProps}>
                  <Button icon={<UploadOutlined />}>Chọn ảnh</Button>
                </Upload>
              </Form.Item>

              <Form.Item
                name="username"
                label="Tên người dùng"
                rules={[
                  { required: true, message: "Vui lòng nhập tên người dùng!" },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item
                name="email"
                label="Email"
                rules={[
                  { required: true, message: "Vui lòng nhập email!" },
                  { type: "email", message: "Email không hợp lệ!" },
                ]}
              >
                <Input />
              </Form.Item>

              <Form.Item name="currentPassword" label="Mật khẩu hiện tại">
                <Input.Password />
              </Form.Item>

              <Form.Item name="newPassword" label="Mật khẩu mới">
                <Input.Password />
              </Form.Item>

              <Form.Item>
                <Button type="primary" htmlType="submit">
                  Cập nhật thông tin
                </Button>
              </Form.Item>
            </Form>
          </TabPane>

          <TabPane tab="Video đã đăng" key="2">
            <List
              itemLayout="horizontal"
              dataSource={[]} // TODO: Implement get user videos API
              renderItem={(video: Video) => (
                <List.Item>
                  <List.Item.Meta
                    avatar={
                      <img
                        src={video.thumbnailUrl}
                        style={{ width: 120, height: 68, objectFit: "cover" }}
                      />
                    }
                    title={video.title}
                    description={
                      <>
                        <p>{video.description}</p>
                        <Tag
                          color={
                            video.status === "READY" ? "success" : "processing"
                          }
                        >
                          {video.status}
                        </Tag>
                        <br />
                        <small>
                          Đăng ngày:{" "}
                          {moment(video.createdAt).format("DD/MM/YYYY HH:mm")}
                        </small>
                      </>
                    }
                  />
                </List.Item>
              )}
            />
          </TabPane>

          <TabPane tab="Lịch sử livestream" key="3">
            <List
              itemLayout="horizontal"
              dataSource={[]} // TODO: Implement get user livestreams API
              renderItem={(stream: LiveStream) => (
                <List.Item>
                  <List.Item.Meta
                    title={stream.title}
                    description={
                      <>
                        <p>{stream.description}</p>
                        <Tag
                          color={
                            stream.status === "LIVE" ? "success" : "default"
                          }
                        >
                          {stream.status}
                        </Tag>
                        <br />
                        <small>
                          {stream.status === "LIVE"
                            ? "Bắt đầu: "
                            : "Đã kết thúc: "}
                          {moment(stream.startedAt).format("DD/MM/YYYY HH:mm")}
                        </small>
                      </>
                    }
                  />
                </List.Item>
              )}
            />
          </TabPane>
        </Tabs>
      </Card>
    </div>
  );
};

export default UserProfile;
