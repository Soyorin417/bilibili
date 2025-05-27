package com.backend.bilibili.controller.activity;

import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.service.activity.PostCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post/comment")
public class PostCommentController {

    @Autowired
    private PostCommentService postCommentService;

    // 新增评论
    @PostMapping("/add")
    public String addComment(@RequestBody PostComment comment) {
        postCommentService.addComment(comment);
        return "评论成功";
    }

    // 根据动态ID获取所有一级评论
    @GetMapping("/list/{postId}")
    public List<PostComment> getComments(@PathVariable Long postId) {
        return postCommentService.getCommentsByPostId(postId);
    }

    // 根据评论ID获取所有回复
    @GetMapping("/replies/{parentId}")
    public List<PostComment> getReplies(@PathVariable Long parentId) {
        return postCommentService.getRepliesByParentId(parentId);
    }

    // 删除评论
    @DeleteMapping("/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        boolean result = postCommentService.deleteCommentById(id);
        return result ? "删除成功" : "删除失败";
    }
}
