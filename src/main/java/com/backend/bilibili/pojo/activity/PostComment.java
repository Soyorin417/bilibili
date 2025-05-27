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
@TableName("post_comment")
public class PostComment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long postId;              // 所属动态ID
    private Long userId;              // 评论用户ID
    private Long parentCommentId;     // 父评论ID（用于子评论）

    private String content;           // 评论内容
    private Integer likes;            // 点赞数
    private Integer status;           // 状态（1=正常，0=删除）

    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
}
