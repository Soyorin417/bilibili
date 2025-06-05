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

export const danmakuApi = {
  // 获取本地弹幕列表
  getDanmakuById: (id) => {
    return api.get(`/danmaku/getDanmakuList?id=${id}`);
  },

  // 获取B站弹幕
  getBilibiliDanmaku: (oid) => {
    return api.get(`/danmaku/getBilibiliDanmaku?oid=${oid}`);
  }
};