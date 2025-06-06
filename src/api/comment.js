import request from "@/utils/request";

export const commentApi = {
  /**
   * 获取评论列表
   * @param {number} videoId - 视频ID
   * @returns {Promise<{data: Array<CommentVO>}>} 评论列表
   */
  getComments(videoId) {
    return request.get("/comments/list", {
      params: { videoId }
    });
  },

  /**
   * 添加评论
   * @param {Object} comment - 评论对象
   * @param {number} comment.videoId - 视频ID
   * @param {number} comment.userId - 用户ID
   * @param {string} comment.content - 评论内容
   * @returns {Promise<{data: string}>} 评论结果
   */
  addComment(comment) {
    return request.post("/comments/add", comment);
  },

  /**
   * 删除评论
   * @param {number} commentId - 评论ID
   * @returns {Promise<{data: string}>} 删除结果
   */
  deleteComment(commentId) {
    return request.delete(`/comments/delete/${commentId}`);
  },

  /**
   * 获取评论回复
   * @param {number} commentId - 评论ID
   * @returns {Promise<{data: Array<ReplyVO>}>} 回复列表
   */
  getReplies(commentId) {
    return request.get("/reply/list", {
      params: { commentId }
    });
  },

  /**
   * 添加回复
   * @param {Object} reply - 回复对象
   * @param {number} reply.commentId - 评论ID
   * @param {number} reply.userId - 用户ID
   * @param {string} reply.content - 回复内容
   * @returns {Promise<{data: string}>} 回复结果
   */
  addReply(reply) {
    return request.post("/reply/add", reply);
  },

  /**
   * 更新回复
   * @param {number} replyId - 回复ID
   * @param {Object} reply - 回复对象
   * @returns {Promise<{data: string}>} 更新结果
   */
  updateReply(replyId, reply) {
    return request.put(`/reply/update/${replyId}`, reply);
  },

  /**
   * 删除回复
   * @param {number} replyId - 回复ID
   * @returns {Promise<{data: string}>} 删除结果
   */
  deleteReply(replyId) {
    return request.delete(`/reply/delete/${replyId}`);
  },

  /**
   * 获取回复详情
   * @param {number} replyId - 回复ID
   * @returns {Promise<{data: ReplyInfo}>} 回复详情
   */
  getReplyById(replyId) {
    return request.get(`/reply/${replyId}`);
  },

  /**
   * 点赞评论
   * @param {number} commentId - 评论ID
   * @param {number} userUid - 用户ID
   * @returns {Promise<{data: string}>} 点赞结果
   */
  likeComment(commentId, userUid) {
    return request.post("/comment-like/like", null, {
      params: { commentId, userUid }
    });
  },

  /**
   * 取消点赞评论
   * @param {number} commentId - 评论ID
   * @param {number} userUid - 用户ID
   * @returns {Promise<{data: string}>} 取消点赞结果
   */
  unlikeComment(commentId, userUid) {
    return request.post("/comment-like/unlike", null, {
      params: { commentId, userUid }
    });
  },

  /**
   * 检查评论点赞状态
   * @param {number} commentId - 评论ID
   * @param {number} userUid - 用户ID
   * @returns {Promise<{data: boolean}>} 是否已点赞
   */
  checkLikeStatus(commentId, userUid) {
    return request.get("/comment-like/is-liked", {
      params: { commentId, userUid }
    });
  }
}; 