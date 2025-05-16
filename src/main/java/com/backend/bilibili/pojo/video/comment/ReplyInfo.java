package com.backend.bilibili.pojo.video.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("reply_info")
public class ReplyInfo {
    @TableId(type = IdType.AUTO)
    private Long id;


    private Long commentId;
    private Long userUid;
    private String content;
    private String replyToUsername;
    private LocalDateTime createTime;
    private Integer likeCount;
    private Boolean isLiked;
}
