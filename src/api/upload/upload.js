import axios from "axios";
// 创建axios实例
const request = axios.create({
  baseURL: "http://localhost:8081",
  timeout: 50000,
});

// 请求拦截器
request.interceptors.request.use(
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
request.interceptors.response.use(
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
// 上传API
export const uploadApi = {
  // 上传文件到MinIO
  uploadToMinIO: (formData, onProgress) => {
    return request.post("/minio/upload", formData, {
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
    return request.post("/video/submit", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      timeout: 10000,
    });
  },

  // 获取上传进度
  getUploadProgress: (uploadId) => {
    return request.get(`/upload/progress/${uploadId}`);
  },

  // 取消上传
  cancelUpload: (uploadId) => {
    return request.delete(`/upload/${uploadId}`);
  },

  // 获取上传历史
  getUploadHistory: () => {
    return request.get("/upload/history");
  },

  // 删除上传记录
  deleteUploadRecord: (uploadId) => {
    return request.delete(`/upload/history/${uploadId}`);
  },

  // 获取上传限制
  getUploadLimits: () => {
    return request.get("/upload/limits");
  },

  // 检查文件是否已存在
  checkFileExists: (fileName) => {
    return request.get("/upload/check", {
      params: { fileName },
    });
  },
};

export default uploadApi;
