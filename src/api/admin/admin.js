import axios from "axios";

// 创建axios实例
const apiInstance = axios.create({
  baseURL: "http://localhost:8081",
  timeout: 15000,
});

// 请求拦截器
apiInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
apiInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // token过期或无效，清除本地存储并跳转到登录页
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// 管理员API
export const adminApi = {
  // 获取所有用户信息
  getAllUserInfo: () => {
    return apiInstance.get("/api/admin/user/getAllUser");
  },

  // 获取用户数量
  getUserCount: () => {
    return apiInstance.get("/api/admin/user/getUserCount");
  },

  // 获取视频数量
  getVideoCount: () => {
    return apiInstance.get("/api/admin/video/getUserCount");
  },

  // 获取待审核视频列表
  getPendingVideos: () => {
    return apiInstance.get("/api/admin/videos/pending");
  },

  // 审核视频
  reviewVideo: (id, status) => {
    return apiInstance.post("/api/admin/videos/review", { id, status });
  },
};

export default adminApi;
