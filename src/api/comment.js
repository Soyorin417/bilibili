import axios from 'axios';

// 创建 axios 实例
const commentApi = axios.create({
  baseURL: 'http://127.0.0.1:8081',
  timeout: 5000
});

// 请求拦截器
commentApi.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 评论相关 API
export default {
  // 获取评论列表
  getComments(videoId) {
    return commentApi.get(`/comments/list?videoId=${videoId}`);
  },

  // 添加评论
  addComment(commentData) {
    return commentApi.post('/comments/add', commentData);
  },

  // 删除评论
  deleteComment(commentId) {
    return commentApi.delete(`/comments/delete/${commentId}`);
  },

  // 获取回复列表
  getReplies(commentId) {
    return commentApi.get(`/reply/list?commentId=${commentId}`);
  },

  // 添加回复
  addReply(replyData) {
    return commentApi.post('/reply/add', replyData);
  },

  // 删除回复
  deleteReply(replyId) {
    return commentApi.delete(`/reply/delete/${replyId}`);
  },

  // 更新回复
  updateReply(replyId, updateData) {
    return commentApi.put(`/reply/update/${replyId}`, updateData);
  },

  // 点赞评论
  likeComment(commentId, userUid) {
    return commentApi.post('/comment-like/like', null, {
      params: { commentId, userUid }
    });
  },

  // 取消点赞评论
  unlikeComment(commentId, userUid) {
    return commentApi.post('/comment-like/unlike', null, {
      params: { commentId, userUid }
    });
  },

  // 检查评论点赞状态
  checkLikeStatus(commentId, userUid) {
    return commentApi.get('/comment-like/is-liked', {
      params: { commentId, userUid }
    });
  }
};
