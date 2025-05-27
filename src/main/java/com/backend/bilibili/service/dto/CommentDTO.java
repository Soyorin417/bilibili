package com.backend.bilibili.service.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long videoId;
    private Long userUid;
    private String content;
}