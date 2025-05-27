package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;               // 动态ID
    private Long userId;           // 用户ID
    private String username;       // 用户名
    private String avatar;         // 用户头像

    private String content;        // 动态文本内容
    private String media;          // 图片或视频链接
    private String duration;       // 视频时长
    private Boolean isImage;       // 是否是图片内容

    private Long views;            // 浏览量
    private Integer commentCount;  // 评论数量
    private Integer likeCount;     // 点赞数量
    private Integer shareCount;    // 转发数量

    private Boolean liked;         // 当前用户是否已点赞
    private Boolean shared;        // 当前用户是否已转发

    private LocalDateTime createTime; // 发布时间
}
