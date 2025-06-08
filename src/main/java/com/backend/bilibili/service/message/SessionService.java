package com.backend.bilibili.service.message;

import com.backend.bilibili.pojo.message.Session;

import java.util.List;

public interface SessionService {

    void addSession(Session session);

    List<Session> getAllSessions();

    Session getSessionById(Long id);

    void deleteSessionById(Long id);

    List<Session> getSessionsByUserId(Long userId);
}
