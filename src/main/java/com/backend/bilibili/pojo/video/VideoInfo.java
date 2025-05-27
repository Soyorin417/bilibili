package com.backend.bilibili.pojo.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("video_info")
public class VideoInfo {
    @TableId(type = IdType.AUTO)
    private int id;

    private String title;
    private Long views;
    private int comments;
    private Date time;
    private String description;

    @TableField("video_url")
    private String videoUrl;

    private String image;

    @TableField("show_right")
    private boolean showRight;

    @TableField("author_id")
    private Long authorId;

    private String duration;

    @TableField("like_count")
    private int likeCount;

    @TableField("collect_count")
    private int collectCount;

    @TableField("coin_count")
    private int coinCount;

    @TableField("share_count")
    private int shareCount;

    private String status;
}
