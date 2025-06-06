package com.backend.bilibili.pojo.carousel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("carousel")
public class Carousel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
    private String type;
}
