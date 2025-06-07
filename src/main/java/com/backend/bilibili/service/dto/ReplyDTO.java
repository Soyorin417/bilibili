package com.backend.bilibili.service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyDTO {
    private Long id;
    private Long commentId;
    private Long userUid;
    private String content;
    private String replyToUsername;
    private LocalDateTime createTime;
    private Integer likeCount;
    private Boolean isLiked;

    // 附加用户信息
    private String avatar;
    private String username;
    private Integer level;
}
