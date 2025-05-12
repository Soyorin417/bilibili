package com.backend.bilibili.pojo.anime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("anime_carousel")
public class AnimeCarousel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
}
