import request from "@/utils/request";

// 消息API
export const messageApi = {
  // 获取所有会话
  getAllSessions: () => {
    return request.get("/sessions");
  },

  // 获取私聊消息
  getPrivateMessages: (user1, user2) => {
    return request.get(`/api/messages/private`, {
      params: { user1, user2 },
    });
  },

  // 发送消息
  sendMessage: (messageData) => {
    return request.post("/api/messages", messageData);
  },

  // 获取会话消息
  getMessagesBySession: (sessionId) => {
    return request.get(`/api/messages/session/${sessionId}`);
  },

  // 获取群聊消息
  getGroupMessages: (groupId) => {
    return request.get(`/messages/group/${groupId}`);
  },

  // 获取未读消息数
  getUnreadCount: () => {
    return request.get("/api/messages/unread/count");
  },

  // 获取会话内未读消息数
  getUnreadCountInSession: (sessionId, userId) => {
    return request.get(`/api/messages/unread/count/${sessionId}`, { params: { userId } });
  },

  // 标记消息为已读
  markMessagesAsRead: (sessionId) => {
    return request.post(`/api/messages/read/${sessionId}`);
  },
  

  // 删除消息
  deleteMessage: (messageId) => {
    return request.delete(`/api/messages/${messageId}`);
  },

  // 获取消息历史记录
  getMessageHistory: (params) => {
    return request.get("/messages/history", { params });
  },

  // 搜索消息
  searchMessages: (keyword) => {
    return request.get("/messages/search", {
      params: { keyword },
    });
  },
};

export default messageApi;
