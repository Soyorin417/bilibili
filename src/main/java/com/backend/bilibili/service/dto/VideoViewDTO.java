package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoViewDTO {
    private int id;
    private String title;
    private Long views;
    private int comments;
    private Date time;
    private String description;
    private String videoUrl;
    private String image;
    private boolean showRight;
    private Long authorId;
    private String duration;
    private int likeCount;
    private int collectCount;
    private int coinCount;
    private int shareCount;
    private String status;
    private String avatar;
    private String follow;
    private int authorFans;
    private String authorName;
}
