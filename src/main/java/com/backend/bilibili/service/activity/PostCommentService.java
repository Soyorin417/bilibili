package com.backend.bilibili.service.activity;

import com.backend.bilibili.pojo.activity.PostComment;
import java.util.List;

public interface PostCommentService {
    void addComment(PostComment comment);
    List<PostComment> getCommentsByPostId(Long postId);
    List<PostComment> getRepliesByParentId(Long parentId);
    boolean deleteCommentById(Long id);
}
