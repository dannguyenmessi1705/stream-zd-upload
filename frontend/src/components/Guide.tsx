import React from "react";
import { Typography, Card, Steps, Divider } from "antd";
import {
  VideoCameraOutlined,
  UploadOutlined,
  PlayCircleOutlined,
  UserOutlined,
} from "@ant-design/icons";

const { Title, Paragraph, Text } = Typography;

const Guide: React.FC = () => {
  return (
    <div style={{ maxWidth: 800, margin: "0 auto", padding: "20px" }}>
      <Typography>
        <Title level={2}>Hướng dẫn sử dụng Video Streaming Platform</Title>

        <Card title="1. Upload Video" style={{ marginBottom: 16 }}>
          <Steps
            direction="vertical"
            current={-1}
            items={[
              {
                title: "Chuẩn bị video",
                description:
                  "Hỗ trợ các định dạng: MP4, AVI, MOV, MKV. Kích thước tối đa: 2GB",
                icon: <VideoCameraOutlined />,
              },
              {
                title: "Upload",
                description:
                  "Truy cập tab Upload, điền thông tin và chọn file video",
                icon: <UploadOutlined />,
              },
              {
                title: "Xử lý",
                description:
                  "Hệ thống sẽ tự động xử lý video, tạo thumbnail và các phiên bản chất lượng khác nhau",
                icon: <VideoCameraOutlined />,
              },
            ]}
          />
        </Card>

        <Card title="2. Livestream" style={{ marginBottom: 16 }}>
          <Steps
            direction="vertical"
            current={-1}
            items={[
              {
                title: "Tạo phòng stream",
                description:
                  "Vào tab Livestream, tạo phòng mới với tiêu đề và mô tả",
                icon: <PlayCircleOutlined />,
              },
              {
                title: "Cấu hình OBS",
                description: (
                  <>
                    <Paragraph>
                      - Server URL: rtmp://localhost:1935/live
                    </Paragraph>
                    <Paragraph>
                      - Stream key: Lấy từ hệ thống sau khi tạo phòng
                    </Paragraph>
                  </>
                ),
                icon: <PlayCircleOutlined />,
              },
              {
                title: "Bắt đầu stream",
                description:
                  "Bắt đầu stream từ OBS, người xem có thể vào xem qua link của bạn",
                icon: <PlayCircleOutlined />,
              },
            ]}
          />
        </Card>

        <Card title="3. Quản lý tài khoản" style={{ marginBottom: 16 }}>
          <Steps
            direction="vertical"
            current={-1}
            items={[
              {
                title: "Đăng ký",
                description: "Tạo tài khoản với email và mật khẩu",
                icon: <UserOutlined />,
              },
              {
                title: "Cập nhật thông tin",
                description: "Thêm avatar và thông tin cá nhân",
                icon: <UserOutlined />,
              },
              {
                title: "Quản lý nội dung",
                description: "Xem và quản lý các video, livestream đã tạo",
                icon: <UserOutlined />,
              },
            ]}
          />
        </Card>

        <Divider />

        <Title level={3}>Thông số kỹ thuật</Title>
        <Card>
          <Paragraph>
            <Text strong>Video Upload:</Text>
            <ul>
              <li>Định dạng hỗ trợ: MP4, AVI, MOV, MKV</li>
              <li>Kích thước tối đa: 2GB</li>
              <li>Độ phân giải tối đa: 4K (3840x2160)</li>
              <li>Chất lượng xuất: 1080p, 720p, 480p</li>
            </ul>
          </Paragraph>

          <Paragraph>
            <Text strong>Livestream:</Text>
            <ul>
              <li>Protocol: RTMP (ingress), HLS (playback)</li>
              <li>Codec: H.264 video, AAC audio</li>
              <li>Bitrate khuyến nghị: 2500-4000 Kbps (1080p)</li>
              <li>Độ trễ trung bình: 2-5 giây</li>
            </ul>
          </Paragraph>

          <Paragraph>
            <Text strong>Yêu cầu hệ thống:</Text>
            <ul>
              <li>Trình duyệt: Chrome, Firefox, Safari phiên bản mới nhất</li>
              <li>Kết nối: Tối thiểu 5Mbps cho upload/stream</li>
              <li>OBS Studio: Phiên bản 29.0 trở lên</li>
            </ul>
          </Paragraph>
        </Card>
      </Typography>
    </div>
  );
};

export default Guide;
