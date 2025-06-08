package com.backend.bilibili.service.message;

import com.backend.bilibili.pojo.message.Message;

import java.util.List;

public interface MessageService {

    void saveMessage(Message message);

    List<Message> getMessagesBySessionId(Long sessionId);

    void deleteMessageById(Long id);

    List<Message> getPrivateMessages(Long user1Id, Long user2Id);

    int getUnreadCount(Long userId);

    int getUnreadCountInSession(Long sessionId, Long userId);

    void markMessagesAsRead(Long sessionId, Long userId);
}
