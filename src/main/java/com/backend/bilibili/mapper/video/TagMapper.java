package com.backend.bilibili.mapper.video;

import com.backend.bilibili.pojo.video.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}

