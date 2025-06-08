package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
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