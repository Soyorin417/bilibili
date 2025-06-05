import request from "@/utils/request";

// 评论相关 API
export default {
  // 获取评论列表
  getComments(videoId) {
    return request.get(`/comments/list?videoId=${videoId}`);
  },

  // 添加评论
  addComment(commentData) {
    return request.post('/comments/add', commentData);
  },

  // 删除评论
  deleteComment(commentId) {
    return request.delete(`/comments/delete/${commentId}`);
  },

  // 获取回复列表
  getReplies(commentId) {
    return request.get(`/reply/list?commentId=${commentId}`);
  },

  // 添加回复
  addReply(replyData) {
    return request.post('/reply/add', replyData);
  },

  // 删除回复
  deleteReply(replyId) {
    return request.delete(`/reply/delete/${replyId}`);
  },

  // 更新回复
  updateReply(replyId, updateData) {
    return request.put(`/reply/update/${replyId}`, updateData);
  },

  // 点赞评论
  likeComment(commentId, userUid) {
    return request.post('/comment-like/like', null, {
      params: { commentId, userUid }
    });
  },

  // 取消点赞评论
  unlikeComment(commentId, userUid) {
    return request.post('/comment-like/unlike', null, {
      params: { commentId, userUid }
    });
  },

  // 检查评论点赞状态
  checkLikeStatus(commentId, userUid) {
    return request.get('/comment-like/is-liked', {
      params: { commentId, userUid }
    });
  }
};
