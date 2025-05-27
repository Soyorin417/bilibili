package com.backend.bilibili.service.activity;

import com.backend.bilibili.pojo.activity.PostLike;

public interface PostLikeService {
    boolean likePost(Long postId, Long userId);
    boolean unlikePost(Long postId, Long userId);
    boolean hasLiked(Long postId, Long userId);
}
