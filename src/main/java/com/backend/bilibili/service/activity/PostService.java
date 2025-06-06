package com.backend.bilibili.service.activity;


import com.backend.bilibili.pojo.activity.Post;
import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.service.dto.PostDTO;

import java.util.List;
import java.util.Map;

public interface PostService {
    Post getPostById(Long id);

    List<Post> getPostsByUserId(Long userId);

    List<Post> searchPostsByContent(String keyword);

    List<PostDTO> getAllPostDTOs(Long currentUserId);


    void addComment(PostComment comment);


    boolean createPost(PostDTO postDTO);  // 使用 DTO 创建

    void updatePost(PostDTO postDTO);  // 使用 DTO 更新

    boolean deletePostById(Long id);

    PostDTO getPostById(Long id, Long currentUserId);
}
