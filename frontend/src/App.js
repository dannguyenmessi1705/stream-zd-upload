import React from "react";
import { BrowserRouter as Router, Routes, Route, Link } from "react-router-dom";
import { Layout, Menu } from "antd";
import {
  UploadOutlined,
  VideoCameraOutlined,
  PlayCircleOutlined,
} from "@ant-design/icons";
import VideoUpload from "./components/VideoUpload";
import VideoList from "./components/VideoList";
import Livestream from "./components/Livestream";

const { Header, Content, Sider } = Layout;

const App = () => {
  return (
    <Router>
      <Layout style={{ minHeight: "100vh" }}>
        <Header style={{ padding: 0, background: "#fff" }}>
          <div style={{ float: "left", marginLeft: 24 }}>
            <h1>Video Streaming Platform</h1>
          </div>
        </Header>
        <Layout>
          <Sider width={200} style={{ background: "#fff" }}>
            <Menu
              mode="inline"
              defaultSelectedKeys={["1"]}
              style={{ height: "100%", borderRight: 0 }}
            >
              <Menu.Item key="1" icon={<VideoCameraOutlined />}>
                <Link to="/">Danh Sách Video</Link>
              </Menu.Item>
              <Menu.Item key="2" icon={<UploadOutlined />}>
                <Link to="/upload">Upload Video</Link>
              </Menu.Item>
              <Menu.Item key="3" icon={<PlayCircleOutlined />}>
                <Link to="/livestream">Livestream</Link>
              </Menu.Item>
            </Menu>
          </Sider>
          <Layout style={{ padding: "24px" }}>
            <Content
              style={{
                background: "#fff",
                padding: 24,
                margin: 0,
                minHeight: 280,
              }}
            >
              <Routes>
                <Route path="/" element={<VideoList />} />
                <Route path="/upload" element={<VideoUpload />} />
                <Route path="/livestream" element={<Livestream />} />
              </Routes>
            </Content>
          </Layout>
        </Layout>
      </Layout>
    </Router>
  );
};

export default App;
