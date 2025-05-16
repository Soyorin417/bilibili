package com.backend.bilibili.controller.video.comment;

import com.backend.bilibili.service.video.comment.CommentLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment-like")
public class CommentLikeController {

    @Autowired
    private CommentLikeService commentLikeService;

    // 点赞接口
    @PostMapping("/like")
    public ResponseEntity<String> likeComment(@RequestParam Long commentId, @RequestParam Long userUid) {
        boolean success = commentLikeService.addLike(commentId, userUid);
        if (success) {
            return ResponseEntity.ok("点赞成功");
        } else {
            return ResponseEntity.badRequest().body("已点赞，不能重复点赞");
        }
    }

    // 取消点赞接口
    @PostMapping("/unlike")
    public ResponseEntity<String> unlikeComment(@RequestParam Long commentId, @RequestParam Long userUid) {
        boolean success = commentLikeService.removeLike(commentId, userUid);
        if (success) {
            return ResponseEntity.ok("取消点赞成功");
        } else {
            return ResponseEntity.badRequest().body("取消点赞失败，可能未点赞过");
        }
    }

    // 查询是否点赞接口
    @GetMapping("/is-liked")
    public ResponseEntity<Boolean> isLiked(@RequestParam Long commentId, @RequestParam Long userUid) {
        boolean liked = commentLikeService.isLiked(commentId, userUid);
        return ResponseEntity.ok(liked);
    }
}
