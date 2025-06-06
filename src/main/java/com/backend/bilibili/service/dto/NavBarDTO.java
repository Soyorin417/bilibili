package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NavBarDTO {
    private int id;              // 视频或动态 ID
    private String title;         // 标题
    private String username;      // 用户名
    private String avatar;        // 用户头像
    private String createTime;    // 发布时间
    private String image;         // 封面图
    private String videoUrl;      // 视频链接
}

