package com.backend.bilibili.service.activity;

import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.service.dto.PostCommentDTO;

import java.util.List;

public interface PostCommentService {
    void addComment(PostComment comment);
    List<PostCommentDTO> getCommentsByPostId(Long postId ,Long userId);
    List<PostComment> getRepliesByParentId(Long parentId);
    boolean deleteCommentById(Long id);
}
