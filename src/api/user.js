import axios from "axios";

// 创建axios实例
const api = axios.create({
  baseURL: "http://127.0.0.1:8081",
  timeout: 5000,
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
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
  // 用户登录
  login: (data) => {
    return api.post("/user/login", data);
  },

  // 用户注册
  register: (data) => {
    return api.post("/user/register", data);
  },

  // 获取用户信息
  getUserInfo: () => {
    return api.get("/user/info");
  },

  // 更新用户信息
  updateUserInfo: (data) => {
    return api.put("/user/update", data);
  },

  // 更新用户头像
  updateAvatar: (formData) => {
    return api.post("/user/avatar", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  },

  // 修改密码
  changePassword: (data) => {
    return api.post("/user/password", data);
  },

  // 获取用户收藏列表
  getFavorites: () => {
    return api.get("/user/favorites");
  },

  // 添加收藏
  addFavorite: (videoId) => {
    return api.post("/user/favorites", { videoId });
  },

  // 取消收藏
  removeFavorite: (videoId) => {
    return api.delete(`/user/favorites/${videoId}`);
  },

  // 获取用户历史记录
  getHistory: () => {
    return api.get("/user/history");
  },

  // 添加历史记录
  addHistory: (videoId) => {
    return api.post("/user/history", { videoId });
  },

  // 清空历史记录
  clearHistory: () => {
    return api.delete("/user/history");
  },

  // 获取用户关注列表
  getFollowings: () => {
    return api.get("/user/followings");
  },

  // 关注用户
  followUser: (followerUid, followingUid, remark = "") => {
    return api.post('/api/follow/follow', null, {
      params: { followerUid, followingUid, remark }
    });
  },

  // 取消关注
  unfollowUser: (followerUid, followingUid) => {
    return api.post('/api/follow/unfollow', null, {
      params: { followerUid, followingUid }
    });
  },

  // 获取用户粉丝列表
  getFollowers: () => {
    return api.get("/user/followers");
  },

  // 获取用户通知
  getNotifications: () => {
    return api.get("/user/notifications");
  },

  // 标记通知为已读
  markNotificationRead: (notificationId) => {
    return api.put(`/user/notifications/${notificationId}/read`);
  },

  // 获取用户统计数据
  getUserStats: () => {
    return api.get("/user/stats");
  },
};

export default userApi;
