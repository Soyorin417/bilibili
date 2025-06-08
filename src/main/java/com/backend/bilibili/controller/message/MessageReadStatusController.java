package com.backend.bilibili.controller.message;

import com.backend.bilibili.pojo.message.MessageReadStatus;
import com.backend.bilibili.service.message.MessageReadStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message/read-status")
public class MessageReadStatusController {

    @Autowired
    private MessageReadStatusService messageReadStatusService;

    @PostMapping("/read")
    public String markMessageAsRead(@RequestParam Long messageId, @RequestParam Long receiverId) {
        messageReadStatusService.markAsRead(messageId, receiverId);
        return "已标记为已读";
    }

    @GetMapping("/unread/{receiverId}")
    public List<MessageReadStatus> getUnreadMessages(@PathVariable Long receiverId) {
        return messageReadStatusService.getUnreadMessages(receiverId);
    }

    @PostMapping("/insert")
    public String insertUnreadStatus(@RequestParam Long messageId, @RequestParam Long receiverId) {
        messageReadStatusService.insertUnreadStatus(messageId, receiverId);
        return "未读状态已插入";
    }
}
