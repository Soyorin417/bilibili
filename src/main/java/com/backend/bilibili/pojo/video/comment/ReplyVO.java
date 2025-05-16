package com.backend.bilibili.pojo.video.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyVO {
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
