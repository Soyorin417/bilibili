import axios from "axios";

// 创建axios实例
const api = axios.create({
  baseURL: "http://127.0.0.1:8081",
  timeout: 5000,
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

export const historyApi = {
    // 获取用户收藏的视频列表
  getUserHistory: () => {
    return api.get(`/api/history`);
  },

  // 收藏视频
  addHistory: (videoId) => {
    return api.post("/api/history/add", { videoId });
  },

    // 取消收藏视频
  removeHistory: (videoId) => {
    return api.post("/api/history/remove", { videoId });
  },

  // 检查视频是否已收藏
  checkHistory: (videoId) => {
    return api.get(`/api/history/check/${videoId}`);
  },
};
