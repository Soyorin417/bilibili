package com.backend.bilibili.service.activity;

public interface PostCommentLikeService {
    void likeComment(Long commentId);
    void unlikeComment(Long commentId);
    boolean isLiked(Long commentId);
    int getLikeCount(Long commentId);
}
