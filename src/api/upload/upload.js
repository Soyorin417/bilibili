import axios from "axios";

// 创建axios实例
const api = axios.create({
  baseURL: "http://127.0.0.1:8081",
  timeout: 300000, // 5分钟超时
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

// 上传API
export const uploadApi = {
  // 上传文件到MinIO
  uploadToMinIO: (formData, onProgress) => {
    return api.post("/minio/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      onUploadProgress: (progressEvent) => {
        if (onProgress) {
          const percentCompleted = Math.round(
            (progressEvent.loaded * 100) / progressEvent.total
          );
          onProgress(percentCompleted);
        }
      },
    });
  },

  // 提交视频信息
  submitVideo: (formData) => {
    return api.post("/video/submit", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      timeout: 10000,
    });
  },

  // 获取上传进度
  getUploadProgress: (uploadId) => {
    return api.get(`/upload/progress/${uploadId}`);
  },

  // 取消上传
  cancelUpload: (uploadId) => {
    return api.delete(`/upload/${uploadId}`);
  },

  // 获取上传历史
  getUploadHistory: () => {
    return api.get("/upload/history");
  },

  // 删除上传记录
  deleteUploadRecord: (uploadId) => {
    return api.delete(`/upload/history/${uploadId}`);
  },

  // 获取上传限制
  getUploadLimits: () => {
    return api.get("/upload/limits");
  },

  // 检查文件是否已存在
  checkFileExists: (fileName) => {
    return api.get("/upload/check", {
      params: { fileName },
    });
  },
};

export default uploadApi;
