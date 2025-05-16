package com.backend.bilibili.pojo.video.comment;

import lombok.Data;

@Data
public class ReplyDTO {
    private Long commentId;
    private Long userUid;
    private String content;
    private String replyToUsername;
}

