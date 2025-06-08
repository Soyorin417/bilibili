package com.backend.bilibili.pojo.message;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message_read_status")
public class MessageReadStatus {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long messageId;
    private Long receiverId;
    private Boolean isRead;
    private LocalDateTime readTime;
}