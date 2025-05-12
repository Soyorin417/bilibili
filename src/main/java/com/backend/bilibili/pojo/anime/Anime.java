package com.backend.bilibili.pojo.anime;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("anime")
public class Anime {
    private int id;
    private String title;
    private String description;
    private String url;
    private int episode;
    private double score;
}
