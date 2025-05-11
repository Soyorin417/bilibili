package com.backend.bilibili.pojo.video.comment.vo;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CommentVO {
    private Long commentId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
    private String avatar;
    private String nickname;
    private String level;
}
