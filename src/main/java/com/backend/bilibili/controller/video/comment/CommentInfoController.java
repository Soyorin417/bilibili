package com.backend.bilibili.controller.video.comment;


import com.backend.bilibili.pojo.video.comment.CommentInfo;
import com.backend.bilibili.pojo.video.comment.CommentVO;
import com.backend.bilibili.service.video.comment.CommentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentInfoController {

    @Autowired
    private CommentInfoService commentService;


    @GetMapping("/list")
    public ResponseEntity<List<CommentVO>> getComments(@RequestParam Long videoId) {
        List<CommentVO> commentList = commentService.getCommentsWithUserInfo(videoId);
        return ResponseEntity.ok(commentList);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentInfo commentInfo) {
        boolean saved = commentService.save(commentInfo);
        if (saved) {
            return ResponseEntity.ok("评论发布成功");
        } else {
            return ResponseEntity.status(500).body("评论发布失败");
        }
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        boolean removed = commentService.deleteCommentById(commentId);
        if (removed) {
            return ResponseEntity.ok("评论删除成功");
        } else {
            return ResponseEntity.status(500).body("评论删除失败");
        }
    }


}
