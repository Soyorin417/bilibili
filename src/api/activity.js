import request from "@/utils/request";

export const activityApi = {
    // 获取用户收藏的视频列表
  getUserActivity: () => {
    return request.get(`/api/activity`);
  },

  // 收藏视频
  addActivity: (videoId) => {
    return request.post("/api/activity/add", { videoId });
  },

    // 取消收藏视频
  removeActivity: (videoId) => {
    return request.post("/api/activity/remove", { videoId });
  },

  // 检查视频是否已收藏
  checkActivity: (videoId) => {
    return request.get(`/api/activity/check/${videoId}`);
  },
};
