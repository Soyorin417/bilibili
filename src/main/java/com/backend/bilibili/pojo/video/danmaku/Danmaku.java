package com.backend.bilibili.pojo.video.danmaku;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("danmaku_list")
public class Danmaku {
    private int vid;
    private String url;
}
