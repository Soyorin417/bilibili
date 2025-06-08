import request from "@/utils/request";

export const commentApi = {

  getComments(videoId) {
    return request.get("/comments/list", {
      params: { videoId }
    });
  },

  addComment(comment) {
    return request.post("/comments/add", comment);
  },


  deleteComment(commentId) {
    return request.delete(`/comments/delete/${commentId}`);
  },


  getReplies(commentId) {
    return request.get("/reply/list", {
      params: { commentId }
    });
  },


  addReply(reply) {
    return request.post("/reply/add", reply);
  },

  updateReply(replyId, reply) {
    return request.put(`/reply/update/${replyId}`, reply);
  },


  deleteReply(replyId) {
    return request.delete(`/reply/delete/${replyId}`);
  },


  getReplyById(replyId) {
    return request.get(`/reply/${replyId}`);
  },

  likeComment(commentId, userUid) {
    return request.post("/comment-like/like", null, {
      params: { commentId, userUid }
    });
  },

  unlikeComment(commentId, userUid) {
    return request.post("/comment-like/unlike", null, {
      params: { commentId, userUid }
    });
  },


  checkLikeStatus(commentId, userUid) {
    return request.get("/comment-like/is-liked", {
      params: { commentId, userUid }
    });
  }
}; 