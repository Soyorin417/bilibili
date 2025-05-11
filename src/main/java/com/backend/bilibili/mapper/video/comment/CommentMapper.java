package com.backend.bilibili.mapper.video.comment;

import com.backend.bilibili.pojo.video.comment.Comment;
import com.backend.bilibili.pojo.video.comment.vo.CommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Select("SELECT " +
            "c.id AS commentId, c.user_id, c.content, c.created_at, " +
            "ui.avatar, ui.username, ui.level " +
            "FROM comment c " +
            "JOIN userinfo ui ON c.user_id = ui.uid " +
            "WHERE c.video_id = #{videoId} " +
            "ORDER BY c.created_at DESC")
    List<CommentVO> getCommentsByVideoId(Long videoId);

}
