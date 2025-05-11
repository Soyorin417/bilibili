package com.backend.bilibili.service.video.comment.impl;

import com.backend.bilibili.mapper.video.comment.CommentMapper;
import com.backend.bilibili.pojo.video.comment.Comment;
import com.backend.bilibili.pojo.video.comment.vo.CommentVO;
import com.backend.bilibili.service.video.comment.CommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getAllCommentsByVideoId(Long videoId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id", videoId);
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public List<CommentVO> getCommentsWithUserInfoByVideoId(Long videoId) {
        return commentMapper.getCommentsByVideoId(videoId);
    }
    @Override
    public boolean addComment(Comment comment) {
        return commentMapper.insert(comment) > 0;
    }

    @Override
    public List<Comment> getCommentsByUserId(Long userId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public boolean deleteComment(Long commentId) {
        return commentMapper.deleteById(commentId) > 0;
    }
}
