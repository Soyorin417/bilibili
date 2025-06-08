package com.backend.bilibili.mapper.video.comment;


import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface CommentInfoMapper extends BaseMapper<CommentInfo> {
    @Update("UPDATE comment_info SET like_count = like_count + 1 WHERE id = #{commentId}")
    void increaseLikeCount(@Param("commentId") Long commentId);

    @Update("UPDATE comment_info SET like_count = like_count - 1 WHERE id = #{commentId} AND like_count > 0")
    void decreaseLikeCount(@Param("commentId") Long commentId);

    @Update("UPDATE comment_info SET reply_count = reply_count + 1 WHERE id = #{commentId}")
    void increaseReplyCount(@Param("commentId") Long commentId);

    @Update("UPDATE comment_info SET reply_count = reply_count - 1 WHERE id = #{commentId} AND reply_count > 0")
    void decreaseReplyCount(@Param("commentId") Long commentId);

}
