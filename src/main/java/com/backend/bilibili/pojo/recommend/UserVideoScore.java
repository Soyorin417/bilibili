package com.backend.bilibili.pojo.recommend;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("user_video_score")
public class UserVideoScore {
    private Long id;
    private Long userId;
    private Long videoId;
    private Double score;
    private LocalDateTime updateTime;
}

