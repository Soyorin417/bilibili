package com.backend.bilibili.pojo.hotSearch;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("hot_search")
public class HotSearch {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String keyword;

    private Integer searchCount;

    private LocalDateTime lastSearchedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
