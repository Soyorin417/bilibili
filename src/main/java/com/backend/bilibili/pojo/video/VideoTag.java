package com.backend.bilibili.pojo.video;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("video_tag")
public class VideoTag {
    private Long videoId;
    private Long tagId;
}

