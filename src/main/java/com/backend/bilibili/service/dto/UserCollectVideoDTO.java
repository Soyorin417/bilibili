package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCollectVideoDTO {
    private Long videoId;         // 视频ID
    private String title;         // 视频标题
    private String image;         // 视频封面
    private String videoUrl;
    private Long views;
    private int comments;
    private String duration;    // 收藏时间（格式化）
}
