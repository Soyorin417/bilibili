package com.backend.bilibili.mapper.video.comment;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.backend.bilibili.pojo.video.comment.CommentLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentLikeMapper extends BaseMapper<CommentLike> {

}
