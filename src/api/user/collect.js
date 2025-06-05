import request from "@/utils/request";

export const collectApi = {
  // 获取用户收藏的视频列表
  getUserCollectedVideos: () => {
    return request.get(`/api/collect`);
    
  },

  getCollectionDetail: () => {
    return request.get(`/api/collect/detail`);
  },

  // 收藏视频
  collectVideo: (videoId) => {
    return request.post("/api/collect/add", { videoId });
  },

  // 取消收藏视频
  uncollectVideo: (videoId) => {
    return request.post("/api/collect/remove", { videoId });
  },

  // 检查视频是否已收藏
  checkVideoCollected: (videoId) => {
    return request.get(`/api/collect/check/${videoId}`);
  },
};
