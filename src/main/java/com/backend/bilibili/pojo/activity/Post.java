package com.backend.bilibili.pojo.activity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String content;

    private String media;

    private String duration;

    private Boolean isImage;

    private Long views;

    private Integer comments;

    private Integer likes;

    private Integer shares;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
