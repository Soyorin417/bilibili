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

// 会话API
export const sessionApi = {
  // 创建会话
  createSession: (data) => api.post("/api/sessions", data),

  // 获取会话列表
  getSessions: () => api.get("/api/sessions"),

  // 获取单个会话
  getSession: (id) => api.get(`/api/sessions/${id}`),

  // 删除会话
  deleteSession: (id) => api.delete(`/api/sessions/${id}`),
};

export default sessionApi;
