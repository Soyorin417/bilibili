package com.backend.bilibili.pojo.video.danmaku;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Danmu {
    private Long id;
    private Long videoId;
    private Long userId;
    private String text;
    private Double timeInVideo;      // 弹幕出现时间，单位秒
    private Integer mode;            // 弹幕类型，对应前端type
    private Integer fontSize = 25;
    private String fontColor = "#ffffff";  // 颜色用字符串存储，如 #00a0ea
    private LocalDateTime sendTime;
    private Integer pool = 0;
    private String userHash;
    private String dmid;             // 弹幕唯一ID
    private Integer weight = 0;
    private Byte status = 1;
    private Double duration = 8.0;   // 持续时间
    private String font = "SimHei";  // 字体
    private Boolean isAdvanced = false;
    private Boolean isScrolling = false;
    private Integer positionX = 50;  // 位置X
    private Integer positionY = 10;  // 位置Y
    private Integer track = 0;       // 轨道
}
