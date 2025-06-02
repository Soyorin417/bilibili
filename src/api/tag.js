import axios from 'axios';
// 创建axios实例
const api = axios.create({
  baseURL: "http://localhost:8081",
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
  (error) => Promise.reject(error)
);

// 响应拦截器
api.interceptors.response.use(
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
// 获取所有标签
export function getAllTags() {
  return api.get('/api/tags');
}

// 根据视频ID获取标签
export function getTagsByVideoId(videoId) {
  return api.get(`/api/tags/video/${videoId}`);
}

// 添加标签
export function createTag(tag) {
  return api.post('/api/tags', tag);
}

// 更新标签
export function updateTag(tag) {
  return api.put('/api/tags', tag);
}

// 删除标签
export function deleteTag(id) {
  return api.delete(`/api/tags/${id}`);
}

// 添加标签到视频
export function addTagToVideo(videoId, tagId) {
  return api.post('/api/video-tags/add', null, { params: { videoId, tagId } });
}

// 移除标签
export function removeTagFromVideo(videoId, tagId) {
  return api.post('/api/video-tags/remove', null, { params: { videoId, tagId } });
}
