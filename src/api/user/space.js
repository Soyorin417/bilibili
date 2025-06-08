import request from "@/utils/request";

export const spaceApi = {
  // 获取当前用户发布的视频列表
  getUserPublishedVideos: (userId) => {
    return request.get("/api/space/videos", {
      params: { userId }
    });
  },
}; 