package com.backend.bilibili.service.activity.impl;

import com.backend.bilibili.mapper.activity.PostLikeMapper;
import com.backend.bilibili.pojo.activity.PostLike;
import com.backend.bilibili.service.activity.PostLikeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostLikeServiceImpl implements PostLikeService {

    @Autowired
    private PostLikeMapper postLikeMapper;

    @Override
    public boolean likePost(Long postId, Long userId) {
        if (hasLiked(postId, userId)) return false;

        PostLike like = new PostLike();
        like.setPostId(postId);
        like.setUserId(userId);
        like.setCreateTime(LocalDateTime.now());
        return postLikeMapper.insert(like) > 0;
    }

    @Override
    public boolean unlikePost(Long postId, Long userId) {
        QueryWrapper<PostLike> query = new QueryWrapper<>();
        query.eq("post_id", postId).eq("user_id", userId);
        return postLikeMapper.delete(query) > 0;
    }

    @Override
    public boolean hasLiked(Long postId, Long userId) {
        QueryWrapper<PostLike> query = new QueryWrapper<>();
        query.eq("post_id", postId).eq("user_id", userId);
        return postLikeMapper.selectCount(query) > 0;
    }
}
