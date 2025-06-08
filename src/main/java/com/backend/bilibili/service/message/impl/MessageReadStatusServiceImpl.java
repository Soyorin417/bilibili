package com.backend.bilibili.service.message.impl;

import com.backend.bilibili.mapper.message.MessageReadStatusMapper;
import com.backend.bilibili.pojo.message.MessageReadStatus;
import com.backend.bilibili.service.message.MessageReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class MessageReadStatusServiceImpl implements MessageReadStatusService {

    @Autowired
    private MessageReadStatusMapper messageReadStatusMapper;

    @Override
    public void markAsRead(Long messageId, Long receiverId) {
        QueryWrapper<MessageReadStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("message_id", messageId).eq("receiver_id", receiverId);
        MessageReadStatus status = messageReadStatusMapper.selectOne(queryWrapper);
        if (status != null && Boolean.FALSE.equals(status.getIsRead())) {
            status.setIsRead(true);
            status.setReadTime(LocalDateTime.now());
            messageReadStatusMapper.updateById(status);
        }
    }

    @Override
    public List<MessageReadStatus> getUnreadMessages(Long receiverId) {
        QueryWrapper<MessageReadStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", receiverId).eq("is_read", false);
        return messageReadStatusMapper.selectList(queryWrapper);
    }

    @Override
    public void insertUnreadStatus(Long messageId, Long receiverId) {
        MessageReadStatus status = new MessageReadStatus();
        status.setMessageId(messageId);
        status.setReceiverId(receiverId);
        status.setIsRead(false);
        messageReadStatusMapper.insert(status);
    }
}
