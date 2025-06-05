import request from "@/utils/request";

export const spaceApi = {
  // 获取用户发布的视频列表
  getUserPublishedVideos: () => {
    return request.get("/api/space/videos");
  }
}; 