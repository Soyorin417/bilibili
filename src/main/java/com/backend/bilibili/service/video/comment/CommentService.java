package com.backend.bilibili.service.video.comment;


import com.backend.bilibili.pojo.video.comment.Comment;
import com.backend.bilibili.pojo.video.comment.vo.CommentVO;

import java.util.List;

public interface CommentService {
    List<CommentVO> getCommentsWithUserInfoByVideoId(Long videoId);



    List<Comment> getAllCommentsByVideoId(Long videoId);


    boolean addComment(Comment comment);


    List<Comment> getCommentsByUserId(Long userId);


    boolean deleteComment(Long commentId);
}
