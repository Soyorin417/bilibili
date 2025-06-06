package com.backend.bilibili.pojo.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user_history")
public class UserHistory {

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("video_id")
    private Long videoId;

    @TableField(value = "watch_time", fill = FieldFill.INSERT)
    private Date watchTime;
}
