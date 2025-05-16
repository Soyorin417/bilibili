package com.backend.bilibili.pojo.video;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("video_action_info")
public class VideoActionInfo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long videoId;
    private Long userUid;

    private Boolean isLike;
    private Boolean isCollect;
    private Boolean isCoined;
    private Boolean isShare;
}
