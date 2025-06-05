import axios from "axios";

// 创建axios实例
const apiInstance = axios.create({
  baseURL: "http://localhost:8081",
  timeout: 15000,
});

// 请求拦截器
apiInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器
apiInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // token过期或无效，清除本地存储并跳转到登录页
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// 用户API
export const userApi = {
  // 获取当前登录用户信息
  getCurrentUserInfo: () => {
    return apiInstance.get("/api/user/info");
  },

  // 根据 uid 获取指定用户信息
  getUserByUid: (uid) => {
    return apiInstance.get(`/api/user/${uid}`);
  },

  // 更新用户信息
  updateUser: (userInfo) => {
    return apiInstance.put("/api/user/update", userInfo);
  },

  // 删除用户
  deleteUser: (uid) => {
    return apiInstance.delete(`/api/user/${uid}`);
  },

  // 更新用户头像等信息
  updateProfile: (formData) => {
    return apiInstance.post("/api/user/update/avatar", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
      timeout: 30000,
    });
  },

  // 用户登录
  login: (data) => {
    return apiInstance.post("/user/login", data);
  },

  // 用户注册
  register: (data) => {
    return apiInstance.post("/user/register", data);
  },

  // 获取用户信息
  getUserInfo: () => {
    return apiInstance.get("/user/info");
  },

  // 更新用户头像
  updateAvatar: (formData) => {
    return apiInstance.post("/user/avatar", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 修改密码
  changePassword: (data) => {
    return apiInstance.post("/user/password", data);
  },

  // 获取用户收藏列表
  getFavorites: () => {
    return apiInstance.get("/user/favorites");
  },

  // 添加收藏
  addFavorite: (videoId) => {
    return apiInstance.post("/user/favorites", { videoId });
  },

  // 取消收藏
  removeFavorite: (videoId) => {
    return apiInstance.delete(`/user/favorites/${videoId}`);
  },

  // 获取用户历史记录
  getHistory: () => {
    return apiInstance.get("/user/history");
  },

  // 添加历史记录
  addHistory: (videoId) => {
    return apiInstance.post("/user/history", { videoId });
  },

  // 清空历史记录
  clearHistory: () => {
    return apiInstance.delete("/user/history");
  },

  // 获取用户关注列表
  getFollowings: () => {
    return apiInstance.get("/user/followings");
  },

  // 关注用户
  followUser: (followerUid, followingUid, remark = "") => {
    return apiInstance.post("/api/follow/follow", null, {
      params: { followerUid, followingUid, remark },
    });
  },

  // 取消关注
  unfollowUser: (followerUid, followingUid) => {
    return apiInstance.post("/api/follow/unfollow", null, {
      params: { followerUid, followingUid },
    });
  },

  // 获取用户粉丝列表
  getFollowers: () => {
    return apiInstance.get("/user/followers");
  },

  // 获取用户通知
  getNotifications: () => {
    return apiInstance.get("/user/notifications");
  },

  // 标记通知为已读
  markNotificationRead: (notificationId) => {
    return apiInstance.put(`/user/notifications/${notificationId}/read`);
  },

  // 获取用户统计数据
  getUserStats: () => {
    return apiInstance.get("/user/stats");
  },
};

export default userApi;
