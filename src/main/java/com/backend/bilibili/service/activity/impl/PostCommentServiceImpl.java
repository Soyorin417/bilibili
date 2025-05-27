package com.backend.bilibili.service.activity.impl;

import com.backend.bilibili.mapper.activity.PostCommentMapper;
import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.service.activity.PostCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentServiceImpl implements PostCommentService {

    @Autowired
    private PostCommentMapper postCommentMapper;

    @Override
    public void addComment(PostComment comment) {
        postCommentMapper.insert(comment);
    }

    @Override
    public List<PostComment> getCommentsByPostId(Long postId) {
        QueryWrapper<PostComment> query = new QueryWrapper<>();
        query.eq("post_id", postId)
                .isNull("parent_comment_id")
                .eq("status", 1)
                .orderByDesc("create_time");
        return postCommentMapper.selectList(query);
    }

    @Override
    public List<PostComment> getRepliesByParentId(Long parentId) {
        QueryWrapper<PostComment> query = new QueryWrapper<>();
        query.eq("parent_comment_id", parentId)
                .eq("status", 1)
                .orderByAsc("create_time");
        return postCommentMapper.selectList(query);
    }

    @Override
    public boolean deleteCommentById(Long id) {
        return postCommentMapper.deleteById(id) > 0;
    }
}
