package com.backend.bilibili.controller.video.comment;



import com.backend.bilibili.service.dto.CommentDTO;
import com.backend.bilibili.service.video.comment.CommentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentInfoController {

    @Autowired
    private CommentInfoService commentService;


    @GetMapping("/list")
    public ResponseEntity<List<CommentDTO>> getComments(@RequestParam Long videoId) {
        List<CommentDTO> commentList = commentService.getCommentsWithUserInfo(videoId);
        return ResponseEntity.ok(commentList);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody Map<String, Object> data) {
        try {
            Long videoId = Long.valueOf(data.get("videoId").toString());
            String content = data.get("content").toString();
            commentService.addComment(videoId, content);
            return ResponseEntity.ok("评论发布成功");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
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

    @GetMapping("/count/{videoId}")
    public int getCommentCount(@PathVariable("videoId") Long videoId) {
        return commentService.getCommentCountByVideoId(videoId);
    }



}
