package com.backend.bilibili.pojo.video;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tag")
public class Tag {
    private Long id;
    private String name;
    private Date createTime;

}