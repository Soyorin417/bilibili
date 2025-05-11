package com.backend.bilibili.controller.video.comment;

import com.backend.bilibili.pojo.video.comment.Comment;
import com.backend.bilibili.pojo.video.comment.vo.CommentVO;
import com.backend.bilibili.service.video.comment.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @GetMapping("/video/{videoId}")
    public List<CommentVO> getComments(@PathVariable Long videoId) {
        return commentService.getCommentsWithUserInfoByVideoId(videoId);
    }

    // 添加评论
    @PostMapping("/add")
    public String addComment(@RequestBody Comment comment) {
        boolean success = commentService.addComment(comment);
        return success ? "添加成功" : "添加失败";
    }

    // 根据用户 ID 获取评论
    @GetMapping("/user/{userId}")
    public List<Comment> getCommentsByUserId(@PathVariable Long userId) {
        return commentService.getCommentsByUserId(userId);
    }

    // 删除评论
    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long commentId) {
        boolean success = commentService.deleteComment(commentId);
        return success ? "删除成功" : "删除失败";
    }
}
