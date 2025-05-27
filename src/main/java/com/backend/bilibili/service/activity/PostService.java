package com.backend.bilibili.service.activity;


import com.backend.bilibili.pojo.activity.Post;
import com.backend.bilibili.service.dto.PostDTO;

import java.util.List;

public interface PostService {
    Post getPostById(Long id);

    List<Post> getPostsByUserId(Long userId);

    List<Post> searchPostsByContent(String keyword);

    List<PostDTO> getAllPostDTOs(Long currentUserId);


    void createPost(PostDTO postDTO);  // 使用 DTO 创建

    void updatePost(PostDTO postDTO);  // 使用 DTO 更新

    void deletePostById(Long id);


    PostDTO getPostById(Long id, Long currentUserId);
}
