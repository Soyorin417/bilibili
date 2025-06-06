package com.backend.bilibili.controller.activity;

import com.backend.bilibili.pojo.activity.Post;
import com.backend.bilibili.service.activity.PostService;
import com.backend.bilibili.service.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    // 根据ID获取动态详情
    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/post/dtoList")
    public List<PostDTO> getAllPostDTOs(@RequestParam Long userId) {
        System.out.println(postService.getPostsByUserId(userId));
        return postService.getAllPostDTOs(userId);
    }

    // 根据用户ID获取该用户的所有动态列表
    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Long userId) {

        return postService.getPostsByUserId(userId);
    }

    // 根据关键字模糊搜索动态内容
    @GetMapping("/search")
    public List<Post> searchPosts(@RequestParam String keyword) {
        return postService.searchPostsByContent(keyword);
    }

    // 新增动态
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO post) {
        boolean success = postService.createPost(post);
        return success
                ? ResponseEntity.ok("动态创建成功")
                : ResponseEntity.badRequest().body("动态创建失败");
    }


    // 更新动态
    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id, @RequestBody PostDTO post) {
        post.setId(id);
        postService.updatePost(post);
        return "动态更新成功";
    }

    // 删除动态
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        boolean success = postService.deletePostById(id);
        return success
                ? ResponseEntity.ok("动态删除成功")
                : ResponseEntity.badRequest().body("删除失败");
    }

}