package com.backend.bilibili.service.activity;

public interface PostLikeService {
    boolean likePost(Long postId, Long userId);
    boolean unlikePost(Long postId, Long userId);
    boolean hasLiked(Long postId, Long userId);
}
