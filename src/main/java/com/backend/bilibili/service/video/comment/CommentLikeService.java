package com.backend.bilibili.service.video.comment;



public interface CommentLikeService {

    boolean addLike(Long commentId, Long userUid);
    boolean removeLike(Long commentId, Long userUid);
    boolean isLiked(Long commentId, Long userUid);
}
