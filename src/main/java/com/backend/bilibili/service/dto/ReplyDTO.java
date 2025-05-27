package com.backend.bilibili.service.dto;

import lombok.Data;

@Data
public class ReplyDTO {
    private Long commentId;
    private Long userUid;
    private String content;
    private String replyToUsername;
}

