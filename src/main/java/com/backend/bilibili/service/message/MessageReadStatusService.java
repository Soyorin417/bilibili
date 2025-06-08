package com.backend.bilibili.service.message;

import com.backend.bilibili.pojo.message.MessageReadStatus;

import java.util.List;

public interface MessageReadStatusService {
    void markAsRead(Long messageId, Long receiverId);
    List<MessageReadStatus> getUnreadMessages(Long receiverId);
    void insertUnreadStatus(Long messageId, Long receiverId);
}
