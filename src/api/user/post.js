import request from "@/utils/request";

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
  // 直接调用后端 add 评论接口
  addComment(data) {
    return request.post('/post/comment/add', data);
  }

};
