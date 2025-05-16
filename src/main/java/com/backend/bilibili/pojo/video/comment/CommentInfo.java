package com.backend.bilibili.pojo.video.comment;

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
@TableName("comment_info")
public class CommentInfo  {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long videoId;

    private Long userUid;

    private String content;

    private LocalDateTime createTime;

    private Integer likeCount;

    private Boolean isUp;

    private Boolean isLiked;

    private Integer replyCount;
}
