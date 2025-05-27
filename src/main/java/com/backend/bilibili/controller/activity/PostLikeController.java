package com.backend.bilibili.controller.activity;

import com.backend.bilibili.service.activity.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post/like")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public String like(@PathVariable Long postId, @RequestParam Long userId) {
        boolean result = postLikeService.likePost(postId, userId);
        return result ? "点赞成功" : "你已经点过赞了";
    }

    @DeleteMapping("/{postId}")
    public String unlike(@PathVariable Long postId, @RequestParam Long userId) {
        boolean result = postLikeService.unlikePost(postId, userId);
        return result ? "取消点赞成功" : "你还没点过赞";
    }

    @GetMapping("/check")
    public boolean hasLiked(@RequestParam Long postId, @RequestParam Long userId) {
        return postLikeService.hasLiked(postId, userId);
    }
}
