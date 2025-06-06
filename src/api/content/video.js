import request from "@/utils/request";

// 视频API
export const videoApi = {
  // 获取所有视频
  getAllVideos: () => {
    return request.get("/video/getAllVideo");
  },

  // 获取视频详情
  getVideoById: (videoId) => {
    return request.get(`/video/getVideoById?id=${videoId}`);
  },

  // 获取视频互动状态
  getVideoActionStatus: (videoId, userUid) => {
    return request.get(`/video/action/status/${videoId}?userUid=${userUid}`);
  },

  // 获取视频标签
  getVideoTags: (videoId) => {
    return request.get(`/api/tags/video/${videoId}`);
  },

  // 获取弹幕列表
  getDanmakuList: (videoId) => {
    return request.get(`/danmaku/getDanmakuList?id=${videoId}`);
  },

  // 获取评论列表
  getCommentList: (videoId) => {
    return request.get(`/comments/list?videoId=${videoId}`);
  },

  // 添加评论
  addComment: (commentData) => {
    return request.post("/comments/add", commentData);
  },

  // 点赞视频
  likeVideo: (videoId) => {
    return request.post(`/video/like/${videoId}`);
  },

  // 取消点赞视频
  unlikeVideo: (videoId) => {
    return request.delete(`/video/like/${videoId}`);
  },

  // 收藏视频
  collectVideo: (videoId) => {
    return request.post(`/video/collect/${videoId}`);
  },

  // 取消收藏视频
  uncollectVideo: (videoId) => {
    return request.delete(`/video/collect/${videoId}`);
  },

  // 投币视频
  coinVideo: (videoId) => {
    return request.post(`/video/coin/${videoId}`);
  },

  // 分享视频
  shareVideo: (videoId) => {
    return request.post(`/video/share/${videoId}`);
  },

  // 获取视频统计数据
  getVideoStats: (videoId) => {
    return request.get(`/video/stats/${videoId}`);
  },

  // 获取相关视频推荐
  getRelatedVideos: (videoId) => {
    return request.get(`/video/related/${videoId}`);
  },

  // 获取热门视频
  getHotVideos: () => {
    return request.get("/video/hot");
  },

  // 获取最新视频
  getLatestVideos: () => {
    return request.get("/video/latest");
  },

  // 获取用户上传的视频
  getUserVideos: (userId) => {
    return request.get(`/video/user/${userId}`);
  },

  // 删除视频
  deleteVideo: (videoId) => {
    return request.delete(`/video/${videoId}`);
  },

  // 更新视频信息
  updateVideo: (videoId, data) => {
    return request.put(`/video/${videoId}`, data);
  },

  // 获取视频卡片列表
  getVideoCards: async () => {
    try {
      const response = await request.get("/video/cards");
      if (response.data && Array.isArray(response.data)) {
        return response.data.map((video) => ({
          id: video.id,
          title: video.title || "未知标题",
          views: video.views || 0,
          comments: video.comments || 0,
          image: video.image || "http://113.45.69.13:9000/image/lucy_moon.jpg",
          duration: video.duration || "00:00",
          author: video.author || "未知作者",
        }));
      }
      throw new Error("Invalid video data format");
    } catch (error) {
      console.error("获取视频列表失败:", error);
      throw error;
    }
  },

  // 获取视频评论
  getVideoComments: (videoId) => {
    return request.get(`/video/${videoId}/comments`);
  },

  // 添加视频评论
  addVideoComment: (videoId, data) => {
    return request.post(`/video/${videoId}/comments`, data);
  },

  // 删除视频评论
  deleteVideoComment: (videoId, commentId) => {
    return request.delete(`/video/${videoId}/comments/${commentId}`);
  },

  // 获取视频分类
  getVideoCategories: () => {
    return request.get("/video/categories");
  },

  // 获取分类下的视频
  getVideosByCategory: (categoryId) => {
    return request.get(`/video/category/${categoryId}`);
  },

  // 搜索视频
  searchVideos: (keyword) => {
    return request.get("/video/search", {
      params: { keyword },
    });
  },

  // 获取推荐视频
  getRecommendedVideos: () => {
    return request.get("/video/recommended");
  },

  // 上报视频播放量
  reportPlay: (videoId) => {
    return request.post(`/video/play/${videoId}`);
  },

  // 关注用户
  followUser: (followerUid, followingUid) => {
    return request.post("/api/follow/follow", null, {
      params: { followerUid, followingUid },
    });
  },

  // 取消关注用户
  unfollowUser: (followerUid, followingUid) => {
    return request.post("/api/follow/unfollow", null, {
      params: { followerUid, followingUid },
    });
  },

  // 上传视频
  uploadVideo: (formData) => {
    return request.post("/video/submit-test", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      timeout: 300000, // 5分钟超时
    });
  },
};

export default videoApi;
