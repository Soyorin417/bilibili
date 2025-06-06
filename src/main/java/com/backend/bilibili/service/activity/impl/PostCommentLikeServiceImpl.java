package com.backend.bilibili.service.activity.impl;

import com.backend.bilibili.mapper.activity.PostCommentLikeMapper;
import com.backend.bilibili.pojo.activity.PostCommentLike;
import com.backend.bilibili.service.activity.PostCommentLikeService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostCommentLikeServiceImpl implements PostCommentLikeService {

    @Autowired
    private PostCommentLikeMapper postCommentLikeMapper;

    @Override
    public void likeComment(Long commentId) {
        Long uid = UserTokenUtil.getUid();

        QueryWrapper<PostCommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId).eq("user_id", uid);
        if (postCommentLikeMapper.selectCount(query) == 0) {
            PostCommentLike like = new PostCommentLike();
            like.setCommentId(commentId);
            like.setUserId(uid);
            like.setCreateTime(LocalDateTime.now());
            postCommentLikeMapper.insert(like);
        }
    }

    @Override
    public void unlikeComment(Long commentId) {
        Long uid = UserTokenUtil.getUid();
        QueryWrapper<PostCommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId).eq("user_id", uid);
        postCommentLikeMapper.delete(query);
    }

    @Override
    public boolean isLiked(Long commentId) {
        Long uid = UserTokenUtil.getUid();
        QueryWrapper<PostCommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId).eq("user_id", uid);
        return postCommentLikeMapper.selectCount(query) > 0;
    }

    @Override
    public int getLikeCount(Long commentId) {
        QueryWrapper<PostCommentLike> query = new QueryWrapper<>();
        query.eq("comment_id", commentId);
        return Math.toIntExact(postCommentLikeMapper.selectCount(query));
    }
}
