package com.backend.bilibili.controller.message;

import com.backend.bilibili.pojo.message.Session;
import com.backend.bilibili.service.message.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {

    @Autowired
    private  SessionService sessionService;


    @GetMapping("/byUserId")
    public List<Session> getSessionsByUserId(@RequestParam Long userId) {
        return sessionService.getSessionsByUserId(userId);
    }

    @PostMapping
    public void saveSession(@RequestBody Session session) {
        sessionService.addSession(session);
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public Session getSession(@PathVariable Long id) {
        return sessionService.getSessionById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSession(@PathVariable Long id) {
        sessionService.deleteSessionById(id);
    }
}
