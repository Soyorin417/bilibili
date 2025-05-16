package com.backend.bilibili.mapper.video.comment;

import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.pojo.video.comment.ReplyVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReplyInfoMapper extends BaseMapper<ReplyInfo> {
    @Select("SELECT r.*, u.username, u.avatar, u.level " +
            "FROM reply_info r " +
            "LEFT JOIN userinfo u ON r.user_uid = u.uid " +
            "WHERE r.comment_id = #{commentId} " +
            "ORDER BY r.create_time ASC")
    List<ReplyVO> selectRepliesWithUserInfo(@Param("commentId") Long commentId);
}
