import request from "@/utils/request";

export const historyApi = {
    // 获取用户收藏的视频列表
  getUserHistory: () => {
    return request.get(`/api/history`);
  },

  // 收藏视频
  addHistory: (videoId) => {
    return request.post("/api/history/add", { videoId });
  },

    // 取消收藏视频
  removeHistory: (videoId) => {
    return request.post("/api/history/remove", { videoId });
  },

  // 检查视频是否已收藏
  checkHistory: (videoId) => {
    return request.get(`/api/history/check/${videoId}`);
  },
};
