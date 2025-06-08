package com.backend.bilibili.service.message.impl;


import com.backend.bilibili.mapper.message.MessagesMapper;
import com.backend.bilibili.mapper.message.SessionMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.message.Message;
import com.backend.bilibili.pojo.message.Session;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.message.SessionService;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private  SessionMapper sessionMapper;

    @Autowired
    private MessagesMapper messagesMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void addSession(Session session) {
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .and(wrapper -> wrapper
                        .eq("user1_id", session.getUser1Id())
                        .eq("user2_id", session.getUser2Id())
                )
                .or(wrapper -> wrapper
                        .eq("user1_id", session.getUser2Id())
                        .eq("user2_id", session.getUser1Id())
                );

        Session existing = sessionMapper.selectOne(queryWrapper);

        if (existing == null) {
            sessionMapper.insert(session);
        }
    }


    @Override
    public List<Session> getAllSessions() {
        return sessionMapper.selectList(null);
    }


    @Override
    public Session getSessionById(Long currentUserId) {
        // 查询用户参与的 Session
        QueryWrapper<Session> wrapper = new QueryWrapper<>();
        wrapper.nested(q -> q.eq("user1_id", currentUserId).or().eq("user2_id", currentUserId));
        Session session = sessionMapper.selectOne(wrapper);

        if (session == null) {
            return null;
        }

        // 判断对方 ID
        Long otherUserId = currentUserId.equals(session.getUser1Id())
                ? session.getUser2Id()
                : session.getUser1Id();

        // 查询对方头像
        UserInfo otherUser = userInfoMapper.selectById(otherUserId);
        if (otherUser != null) {
            session.setAvatar(otherUser.getAvatar());
        }

        return session;
    }



    @Override
    public void deleteSessionById(Long id) {
        sessionMapper.deleteById(id);
    }

    @Override
    public List<Session> getSessionsByUserId(Long userId) {
        // 查询当前用户参与的所有会话
        QueryWrapper<Session> wrapper = new QueryWrapper<>();
        wrapper.nested(q -> q.eq("user1_id", userId).or().eq("user2_id", userId));
        List<Session> sessions = sessionMapper.selectList(wrapper);

        // 遍历会话列表，填充头像、昵称、最后消息摘要
        for (Session session : sessions) {
            // 找到对方用户 ID
            Long otherUserId = userId.equals(session.getUser1Id()) ? session.getUser2Id() : session.getUser1Id();

            // 查询对方用户信息（头像、昵称）
            UserInfo otherUser = userInfoMapper.selectById(otherUserId);
            if (otherUser != null) {
                session.setAvatar(otherUser.getAvatar());
                session.setName(otherUser.getUsername());
            }

            // 查询该会话最后一条消息内容，做为摘要显示
            QueryWrapper<Message> msgWrapper = new QueryWrapper<>();
            msgWrapper.eq("session_id", session.getId())
                    .orderByDesc("send_time")
                    .last("LIMIT 1");
            Message lastMsg = messagesMapper.selectOne(msgWrapper);

            if (lastMsg != null) {
                session.setSummary(lastMsg.getContent());
            }
        }
        System.out.println(sessions);

        return sessions;
    }

}
