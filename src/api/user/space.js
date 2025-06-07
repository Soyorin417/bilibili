import request from "@/utils/request";

export const spaceApi = {
  // 获取当前用户发布的视频列表
  getUserPublishedVideos: () => {
    return request.get("/api/space/videos");
  },

  // 获取其他用户发布的视频列表
  getOtherUserPublishedVideos: (userId) => {
    return request.get("/api/space/videos/other", {
      params: { userId }
    });
  }
}; 