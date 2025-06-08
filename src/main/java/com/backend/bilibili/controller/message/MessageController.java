package com.backend.bilibili.controller.message;

import com.backend.bilibili.pojo.message.Message;
import com.backend.bilibili.service.message.MessageService;
import com.backend.bilibili.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public void saveMessage(@RequestBody Message message) {
        message.setSendTime(new Date());
        message.setIsRead(false);
        messageService.saveMessage(message);
    }

    @GetMapping("/session/{sessionId}")
    public List<Message> getMessagesBySession(@PathVariable Long sessionId) {
        return messageService.getMessagesBySessionId(sessionId);
    }

    @GetMapping("/private")
    public List<Message> getPrivateMessages(@RequestParam Long user1, @RequestParam Long user2) {
        return messageService.getPrivateMessages(user1, user2);
    }

    @DeleteMapping("/{id}")
    public void deleteMessage(@PathVariable Long id) {
        messageService.deleteMessageById(id);
    }

    @GetMapping("/unread/count")
    public int getUnreadCount() {
        Long userId = UserTokenUtil.getUid();
        return messageService.getUnreadCount(userId);
    }

    @GetMapping("/unread/count/{sessionId}")
    public int getUnreadCountInSession(@PathVariable Long sessionId, @RequestParam Long userId) {
        return messageService.getUnreadCountInSession(sessionId, userId);
    }

    @PostMapping("/read/{sessionId}")
    public void markMessagesAsRead(@PathVariable Long sessionId) {
        Long userId = UserTokenUtil.getUid();
        System.out.println("session" + sessionId);
        System.out.println("user" + userId);
        messageService.markMessagesAsRead(sessionId, userId);
    }
}

