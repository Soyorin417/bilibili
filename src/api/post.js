import request from "@/utils/request";

export const postApi = {
  likePost(postId, userId) {
    return request.post(`/post/like/${postId}`, null, {
      params: { userId },
    });
  },

  unlikePost(postId, userId) {
    return request.delete(`/post/like/${postId}`, {
      params: { userId },
    });
  },

  /**
   * 获取评论列表
   * @param {number} postId - 动态ID
   * @param {number} [uid] - 用户ID（可选，用于获取用户点赞状态）
   * @returns {Promise<{data: Array<PostCommentDTO>}>} 评论列表
   */
  getComments(postId, uid) {
    return request.get(`/post/comment/list/${postId}`, {
      params: { uid },
    });
  },

  /**
   * 添加评论
   * @param {Object} comment - 评论对象
   * @param {number} comment.postId - 所属动态ID
   * @param {number} [comment.parentCommentId] - 父评论ID（可选，用于回复）
   * @param {string} comment.content - 评论内容
   * @returns {Promise<{data: string}>} 评论结果
   */
  addComment(comment) {
    return request.post("/post/comment/add", {
      postId: comment.postId,
      parentCommentId: comment.parentCommentId,
      content: comment.content,
    });
  },

  /**
   * 删除评论
   * @param {number} commentId - 评论ID
   * @returns {Promise<{data: string}>} 删除结果
   */
  deleteComment(commentId) {
    return request.delete(`/post/comment/delete/${commentId}`);
  },

  /**
   * 获取评论回复
   * @param {number} parentCommentId - 父评论ID
   * @returns {Promise<{data: Array<PostComment>}>} 回复列表
   */
  getReplies(parentCommentId) {
    return request.get(`/post/comment/replies/${parentCommentId}`);
  },

  /**
   * 点赞评论
   * @param {number} commentId - 评论ID
   * @returns {Promise<{data: string}>} 点赞结果
   */
  likeComment(commentId) {
    return request.post(`/post/comment/like/${commentId}`);
  },

  /**
   * 取消点赞评论
   * @param {number} commentId - 评论ID
   * @returns {Promise<{data: string}>} 取消点赞结果
   */
  unlikeComment(commentId) {
    return request.delete(`/post/comment/like/${commentId}`);
  },
};
