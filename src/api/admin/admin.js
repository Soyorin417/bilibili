import request from "@/utils/request";

// 管理员API
export const adminApi = {
  // 获取所有用户信息
  getAllUserInfo: () => {
    return request.get("/api/admin/user/getAllUser");
  },

  // 获取用户数量
  getUserCount: () => {
    return request.get("/api/admin/user/getUserCount");
  },

  // 获取视频数量
  getVideoCount: () => {
    return request.get("/api/admin/video/getUserCount");
  },

  // 获取待审核视频列表
  getPendingVideos: () => {
    return request.get("/api/admin/videos/pending");
  },

  // 审核视频
  reviewVideo: (id, status) => {
    return request.post("/api/admin/videos/review", { id, status });
  },
};

export default adminApi;
