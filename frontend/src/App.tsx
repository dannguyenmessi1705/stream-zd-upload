import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
  useNavigate,
} from "react-router-dom";
import { Layout, Menu, Button, Dropdown, Space } from "antd";
import {
  VideoCameraOutlined,
  UploadOutlined,
  PlayCircleOutlined,
  UserOutlined,
  QuestionCircleOutlined,
  LogoutOutlined,
  LoginOutlined,
} from "@ant-design/icons";
import VideoList from "./components/VideoList";
import VideoUpload from "./components/VideoUpload";
import Livestream from "./components/Livestream";
import UserProfile from "./components/UserProfile";
import Guide from "./components/Guide";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import PrivateRoute from "./components/auth/PrivateRoute";
import { AuthProvider, useAuth } from "./contexts/AuthContext";

const { Header, Content } = Layout;

const HeaderMenu: React.FC = () => {
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const userMenuItems = [
    {
      key: "profile",
      label: <Link to="/profile">Trang cá nhân</Link>,
      icon: <UserOutlined />,
    },
    {
      key: "logout",
      label: "Đăng xuất",
      icon: <LogoutOutlined />,
      danger: true,
      onClick: () => {
        logout();
        navigate("/");
      },
    },
  ];

  return (
    <Header
      style={{
        position: "fixed",
        zIndex: 1,
        width: "100%",
        padding: "0 24px",
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
      }}
    >
      <div style={{ flex: 1 }}>
        <Menu theme="dark" mode="horizontal" defaultSelectedKeys={["1"]}>
          <Menu.Item key="1" icon={<VideoCameraOutlined />}>
            <Link to="/">Videos</Link>
          </Menu.Item>
          {isAuthenticated && (
            <>
              <Menu.Item key="2" icon={<UploadOutlined />}>
                <Link to="/upload">Upload</Link>
              </Menu.Item>
              <Menu.Item key="3" icon={<PlayCircleOutlined />}>
                <Link to="/livestream">Livestream</Link>
              </Menu.Item>
            </>
          )}
          <Menu.Item key="4" icon={<QuestionCircleOutlined />}>
            <Link to="/guide">Hướng dẫn</Link>
          </Menu.Item>
        </Menu>
      </div>

      <div>
        {isAuthenticated ? (
          <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
            <Button type="text" style={{ color: "white" }}>
              <Space>
                <UserOutlined />
                {user?.username}
              </Space>
            </Button>
          </Dropdown>
        ) : (
          <Space>
            <Button
              type="text"
              icon={<LoginOutlined />}
              style={{ color: "white" }}
            >
              <Link to="/login">Đăng nhập</Link>
            </Button>
            <Button type="primary">
              <Link to="/register">Đăng ký</Link>
            </Button>
          </Space>
        )}
      </div>
    </Header>
  );
};

const App: React.FC = () => {
  return (
    <AuthProvider>
      <Router>
        <Layout style={{ minHeight: "100vh" }}>
          <HeaderMenu />
          <Content style={{ padding: "24px", marginTop: 64 }}>
            <Routes>
              <Route path="/" element={<VideoList />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/guide" element={<Guide />} />

              <Route
                path="/upload"
                element={
                  <PrivateRoute>
                    <VideoUpload />
                  </PrivateRoute>
                }
              />
              <Route
                path="/livestream"
                element={
                  <PrivateRoute>
                    <Livestream />
                  </PrivateRoute>
                }
              />
              <Route
                path="/profile"
                element={
                  <PrivateRoute>
                    <UserProfile />
                  </PrivateRoute>
                }
              />
            </Routes>
          </Content>
        </Layout>
      </Router>
    </AuthProvider>
  );
};

export default App;
