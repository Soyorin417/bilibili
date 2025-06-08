import request from "@/utils/request";

export const messageReadStatusApi = {

  markMessageAsRead: (messageId, receiverId) => {
    return request.post("/message/read-status/read", null, {
      params: { messageId, receiverId },
    });
  },

  getUnreadMessages: (receiverId) => {
    return request.get(`/message/read-status/unread/${receiverId}`);
  },

  insertUnreadStatus: (messageId, receiverId) => {
    return request.post("/message/read-status/insert", null, {
      params: { messageId, receiverId },
    });
  },
}; 