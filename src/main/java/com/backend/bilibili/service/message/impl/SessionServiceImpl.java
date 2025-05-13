package com.backend.bilibili.service.message.impl;


import com.backend.bilibili.mapper.message.SessionMapper;
import com.backend.bilibili.pojo.message.Session;
import com.backend.bilibili.service.message.SessionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionMapper sessionMapper;

    public SessionServiceImpl(SessionMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    @Override
    public void saveSession(Session session) {
        sessionMapper.insert(session);
    }

    @Override
    public List<Session> getAllSessions() {
        return sessionMapper.selectList(null);
    }


    @Override
    public Session getSessionById(Long user1Id) {
        QueryWrapper<Session> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user1_id", user1Id);
        return sessionMapper.selectOne(queryWrapper);
    }

    @Override
    public void deleteSessionById(Long id) {
        sessionMapper.deleteById(id);
    }
}
