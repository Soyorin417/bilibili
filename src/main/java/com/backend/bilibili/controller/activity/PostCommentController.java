package com.backend.bilibili.controller.activity;

import com.backend.bilibili.pojo.activity.PostComment;
import com.backend.bilibili.service.activity.PostCommentLikeService;
import com.backend.bilibili.service.activity.PostCommentService;
import com.backend.bilibili.service.dto.PostCommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/post/comment")
public class PostCommentController {

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private PostCommentLikeService postCommentLikeService;


    @GetMapping("/list/{postId}")
    public List<PostCommentDTO> getComments(@PathVariable Long postId,
                                            @RequestParam(required = false) Long uid) {
        return postCommentService.getCommentsByPostId(postId, uid);
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

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody PostComment comment) {
        try {
            postCommentService.addComment(comment);
            return ResponseEntity.ok("评论成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("评论失败");
        }
    }

    @PostMapping("/like/{commentId}")
    public ResponseEntity<String> likeComment(@PathVariable Long commentId) {
        postCommentLikeService.likeComment(commentId);
        return ResponseEntity.ok("点赞成功");
    }

    @DeleteMapping("/like/{commentId}")
    public ResponseEntity<String> unlikeComment(@PathVariable Long commentId) {
        postCommentLikeService.unlikeComment(commentId);
        return ResponseEntity.ok("取消点赞成功");
    }

}
