package com.backend.bilibili.controller.message;

import com.backend.bilibili.pojo.message.Message;
import com.backend.bilibili.service.message.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public void saveMessage(@RequestBody Message message) {
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
}
