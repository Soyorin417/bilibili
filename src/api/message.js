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
      localStorage.removeItem("token");
      localStorage.removeItem("userInfo");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  }
);

// 消息API
export const messageApi = {
  // 获取所有会话
  getAllSessions: () => {
    return api.get("/sessions");
  },

  // 获取私聊消息
  getPrivateMessages: (user1, user2) => {
    return api.get(`/messages/private`, {
      params: { user1, user2 },
    });
  },

  // 发送消息
  sendMessage: (messageData) => {
    return api.post("/messages", messageData);
  },

  // 获取群聊消息
  getGroupMessages: (groupId) => {
    return api.get(`/messages/group/${groupId}`);
  },

  // 获取未读消息数
  getUnreadCount: () => {
    return api.get("/messages/unread/count");
  },

  // 标记消息为已读
  markAsRead: (messageIds) => {
    return api.put("/messages/read", { messageIds });
  },

  // 删除消息
  deleteMessage: (messageId) => {
    return api.delete(`/messages/${messageId}`);
  },

  // 获取消息历史记录
  getMessageHistory: (params) => {
    return api.get("/messages/history", { params });
  },

  // 搜索消息
  searchMessages: (keyword) => {
    return api.get("/messages/search", {
      params: { keyword },
    });
  },
};

export default messageApi;
