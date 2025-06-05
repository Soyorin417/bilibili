import axios from 'axios';

// 创建 axios 实例
const request = axios.create({
  baseURL: 'http://127.0.0.1:8081',
  timeout: 5000
});

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
request.interceptors.response.use(
  response => response.data,
  error => {
    return Promise.reject(error);
  }
);

export default {
  // 获取动态列表
  getPosts(userId) {
    return request.get('/posts/post/dtoList', {
      params: { userId }
    });
  },

  // 创建动态
  createPost(data) {
    return request.post('/posts', data);
  },

  // 删除动态
  deletePost(id) {
    return request.delete(`/posts/${id}`);
  },

  // 更新动态
  updatePost(id, data) {
    return request.put(`/posts/${id}`, data);
  },

  // 点赞动态
  likePost(id) {
    return request.post(`/posts/${id}/like`);
  },

  // 取消点赞
  unlikePost(id) {
    return request.delete(`/posts/${id}/like`);
  },

  // 分享动态
  sharePost(id) {
    return request.post(`/posts/${id}/share`);
  },

  // 获取动态评论
  getComments(postId) {
    return request.get(`/posts/${postId}/comments`);
  },

  // 添加评论
  addComment(postId, data) {
    return request.post(`/posts/${postId}/comments`, data);
  }
};
