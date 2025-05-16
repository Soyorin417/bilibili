package com.backend.bilibili.mapper.video.comment;


import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.pojo.video.comment.CommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentInfoMapper extends BaseMapper<CommentInfo> {
    @Select("SELECT c.*, u.username, u.avatar, u.level " +
            "FROM comment_info c " +
            "LEFT JOIN userinfo u ON c.user_uid = u.uid " +
            "WHERE c.video_id = #{videoId} " +
            "ORDER BY c.create_time DESC")
    List<CommentVO> selectCommentsWithUserInfo(@Param("videoId") Long videoId);
    @Update("UPDATE comment_info SET like_count = like_count + 1 WHERE id = #{commentId}")
    void increaseLikeCount(@Param("commentId") Long commentId);

    @Update("UPDATE comment_info SET like_count = like_count - 1 WHERE id = #{commentId} AND like_count > 0")
    void decreaseLikeCount(@Param("commentId") Long commentId);

    @Update("UPDATE comment_info SET reply_count = reply_count + 1 WHERE id = #{commentId}")
    void increaseReplyCount(@Param("commentId") Long commentId);

    @Update("UPDATE comment_info SET reply_count = reply_count - 1 WHERE id = #{commentId} AND reply_count > 0")
    void decreaseReplyCount(@Param("commentId") Long commentId);

}
