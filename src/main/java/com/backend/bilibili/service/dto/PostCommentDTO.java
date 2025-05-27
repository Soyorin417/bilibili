package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String avatar;
    private String content;
    private LocalDateTime createTime;
    private Integer likeCount;
    private Boolean liked; // 当前用户是否点赞
    private List<PostCommentDTO> replies; // 子评论
}
