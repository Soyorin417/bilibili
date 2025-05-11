package com.backend.bilibili.pojo.video.comment;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("comment")
public class Comment {
    private Long id;               // 评论ID
    private String content;        // 评论内容
    private int userId;           // 用户ID（外键）
    private int videoId;          // 视频ID（外键）
    private Long parentId;         // 父评论ID（用于楼中楼）
    private LocalDateTime createdAt; //
}
