package com.backend.bilibili.service.message.impl;

import com.backend.bilibili.mapper.message.MessagesMapper;
import com.backend.bilibili.mapper.message.SessionMapper;
import com.backend.bilibili.pojo.message.Message;
import com.backend.bilibili.pojo.message.Session;
import com.backend.bilibili.service.message.MessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private  MessagesMapper messagesMapper;

    @Autowired
    private  SessionMapper sessionMapper;

    public MessageServiceImpl(MessagesMapper messagesMapper) {
        this.messagesMapper = messagesMapper;
    }

    @Override
    public void saveMessage(Message message) {
        messagesMapper.insert(message);
    }

    @Override
    public List<Message> getMessagesBySessionId(Long sessionId) {

        return messagesMapper.getMessagesBySessionId(sessionId);
    }

    public List<Message> getPrivateMessages(Long user1Id, Long user2Id) {
        // 查询会话ID
        Session session = sessionMapper.selectOne(new QueryWrapper<Session>()
                .eq("user1_id", user1Id)
                .eq("user2_id", user2Id)
                .or()
                .eq("user1_id", user2Id)
                .eq("user2_id", user1Id)
        );

        if (session == null) {
            return new ArrayList<>();
        }

        // 查询该会话的所有消息
        QueryWrapper<Message> messageWrapper = new QueryWrapper<>();
        messageWrapper.eq("session_id", session.getId())
                .orderByAsc("send_time");  // 按发送时间排序

        return messagesMapper.selectList(messageWrapper);
    }




    @Override
    public void deleteMessageById(Long id) {
        messagesMapper.deleteById(id);
    }


}
