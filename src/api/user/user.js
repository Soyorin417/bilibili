import request from "@/utils/request";

// 用户API
export const userApi = {
  // 获取当前登录用户信息
  getCurrentUserInfo: () => {
    return request.get("/api/user/info");
  },

  // 根据 uid 获取指定用户信息
  getUserByUid: (uid) => {
    return request.get(`/api/user/${uid}`);
  },

  // 更新用户信息
  updateUser: (userInfo) => {
    return request.put("/api/user/update", userInfo);
  },

  // 删除用户
  deleteUser: (uid) => {
    return request.delete(`/api/user/${uid}`);
  },

  // 更新用户头像等信息
  updateProfile: (formData) => {
    return request.post("/api/user/update/avatar", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      timeout: 30000,
    });
  },

  // 用户登录
  login: (data) => {
    return request.post("/user/login", data);
  },

  // 用户注册
  register: (data) => {
    return request.post("/user/register", data);
  },

  // 获取用户信息
  getUserInfo: () => {
    return request.get("/user/info");
  },

  // 更新用户头像
  updateAvatar: (formData) => {
    return request.post("/user/avatar", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 修改密码
  changePassword: (data) => {
    return request.post("/user/password", data);
  },

  // 获取用户收藏列表
  getFavorites: () => {
    return request.get("/user/favorites");
  },

  // 添加收藏
  addFavorite: (videoId) => {
    return request.post("/user/favorites", { videoId });
  },

  // 取消收藏
  removeFavorite: (videoId) => {
    return request.delete(`/user/favorites/${videoId}`);
  },

  // 获取用户历史记录
  getHistory: () => {
    return request.get("/user/history");
  },

  // 添加历史记录
  addHistory: (videoId) => {
    return request.post("/user/history", { videoId });
  },

  // 清空历史记录
  clearHistory: () => {
    return request.delete("/user/history");
  },

  // 获取用户关注列表
  getFollowings: () => {
    return request.get("/user/followings");
  },

  // 关注用户
  followUser: (followerUid, followingUid, remark = "") => {
    return request.post("/api/follow/follow", null, {
      params: { followerUid, followingUid, remark },
    });
  },

  // 取消关注
  unfollowUser: (followerUid, followingUid) => {
    return request.post("/api/follow/unfollow", null, {
      params: { followerUid, followingUid },
    });
  },

  // 获取用户粉丝列表
  getFollowers: () => {
    return request.get("/user/followers");
  },

  // 获取用户通知
  getNotifications: () => {
    return request.get("/user/notifications");
  },

  // 标记通知为已读
  markNotificationRead: (notificationId) => {
    return request.put(`/user/notifications/${notificationId}/read`);
  },

  // 获取用户统计数据
  getUserStats: () => {
    return request.get("/user/stats");
  },
};

export default userApi;
