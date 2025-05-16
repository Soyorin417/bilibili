package com.backend.bilibili.pojo.video.comment;

import lombok.Data;

@Data
public class CommentDTO {
    private Long videoId;
    private Long userUid;
    private String content;
}