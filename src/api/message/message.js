import request from "@/utils/request";

// 消息API
export const messageApi = {
  // 获取所有会话
  getAllSessions: () => {
    return request.get("/sessions");
  },

  // 获取私聊消息
  getPrivateMessages: (user1, user2) => {
    return request.get(`/messages/private`, {
      params: { user1, user2 },
    });
  },

  // 发送消息
  sendMessage: (messageData) => {
    return request.post("/messages", messageData);
  },

  // 获取群聊消息
  getGroupMessages: (groupId) => {
    return request.get(`/messages/group/${groupId}`);
  },

  // 获取未读消息数
  getUnreadCount: () => {
    return request.get("/messages/unread/count");
  },

  // 标记消息为已读
  markAsRead: (messageIds) => {
    return request.put("/messages/read", { messageIds });
  },

  // 删除消息
  deleteMessage: (messageId) => {
    return request.delete(`/messages/${messageId}`);
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
