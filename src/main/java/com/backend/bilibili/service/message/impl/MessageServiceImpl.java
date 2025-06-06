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
    private MessagesMapper messagesMapper;

    @Autowired
    private SessionMapper sessionMapper;

    @Override
    public void saveMessage(Message message) {
        messagesMapper.insert(message);
    }

    @Override
    public List<Message> getMessagesBySessionId(Long sessionId) {
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.eq("session_id", sessionId)
                .orderByAsc("send_time");
        return messagesMapper.selectList(wrapper);
    }

    @Override
    public List<Message> getPrivateMessages(Long user1Id, Long user2Id) {
        // 使用 nested 保证 or 条件被正确括号包围，避免SQL逻辑错误
        List<Session> sessions = sessionMapper.selectList(
                new QueryWrapper<Session>().nested(w ->
                        w.eq("user1_id", user1Id).eq("user2_id", user2Id)
                                .or()
                                .eq("user1_id", user2Id).eq("user2_id", user1Id)
                )
        );

        // 如果没有找到会话，返回空列表
        if (sessions.isEmpty()) {
            return new ArrayList<>();
        }

        // 使用第一个找到的会话（应该只有一个）
        Session session = sessions.get(0);

        // 查询该会话的所有消息
        QueryWrapper<Message> messageWrapper = new QueryWrapper<>();
        messageWrapper.eq("session_id", session.getId())
                .orderByAsc("send_time");

        return messagesMapper.selectList(messageWrapper);
    }
    @Override
    public void deleteMessageById(Long id) {
        messagesMapper.deleteById(id);
    }
}
