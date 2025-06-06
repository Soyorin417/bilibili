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

  // 通过用户ID查用户
  getUserById: (uid) => {
    return request.get("/api/admin/user/getById", {
      params: { uid },
    });
  },

  // 通过用户名查用户
  getUserByUsername: (username) => {
    return request.get("/api/admin/user/getByUsername", {
      params: { username },
    });
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

  // 封禁用户
  banUser: (uid) => {
    return request.post("/api/admin/ban", { uid });
  },

  // 解封用户
  unbanUser: (uid) => {
    return request.post("/api/admin/unban", { uid });
  },
};

export default adminApi;
