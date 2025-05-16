package com.backend.bilibili.pojo.video.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentVO {
    private Long id;
    private Integer videoId;
    private Long userUid;
    private String content;
    private LocalDateTime createTime;
    private Integer likeCount;
    private Boolean isUp;
    private Boolean isLiked;
    private Integer replyCount;

    private String avatar;
    private String username;
    private Integer level;
}
