package com.backend.bilibili.pojo.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("video_info")
public class VideoInfo {
    @TableId(type = IdType.AUTO)
    private int id;
    private String title;
    private String views;
    private String comments;
    private String time;
    private String description;
    private String avatar;
    private String videoUrl;
    private String image;
    private boolean showRight;
    private String author;
    private String follow;
    private boolean isLike;
    private boolean isDislike;
    private boolean isCollect;
    private boolean isCoined;
    private boolean isShare;
    private String duration;
    private int likeCount;
    private int collectCount;
    private int coinCount;
    private int shareCount;

}
