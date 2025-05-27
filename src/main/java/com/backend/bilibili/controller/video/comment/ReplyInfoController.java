package com.backend.bilibili.controller.video.comment;

import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.vo.ReplyVO;
import com.backend.bilibili.service.video.comment.ReplyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyInfoController {

    @Autowired
    private ReplyInfoService replyInfoService;

    // 获取某条评论下的所有回复
    @GetMapping("/list")
    public ResponseEntity<List<ReplyVO>> getReplies(@RequestParam Long commentId) {
        List<ReplyVO> replies = replyInfoService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }

    // 新增一条回复
    @PostMapping("/add")
    public ResponseEntity<String> addReply(@RequestBody ReplyVO replyVO) {
        boolean success = replyInfoService.saveReply(replyVO);
        if (success) {
            return ResponseEntity.ok("回复添加成功");
        } else {
            return ResponseEntity.badRequest().body("添加失败");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReply(@PathVariable Long id, @RequestBody ReplyVO replyVO) {
        if (!id.equals(replyVO.getId())) {
            System.out.println("路径id和请求体id不一致");
            return ResponseEntity.badRequest().body("路径ID与实体ID不一致");
        }

        boolean success = replyInfoService.updateReplyById(id, replyVO);
        System.out.println("更新结果：" + success);
        if (success) {
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.badRequest().body("更新失败");
        }
    }



    // 删除回复
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReply(@PathVariable Long id) {
        boolean success = replyInfoService.deleteReplyById(id);
        if (success) {
            return ResponseEntity.ok("回复删除成功");
        } else {
            return ResponseEntity.badRequest().body("删除失败");
        }
    }

    // 获取单条回复详情
    @GetMapping("/{id}")
    public ResponseEntity<ReplyInfo> getReplyById(@PathVariable Long id) {
        ReplyInfo reply = replyInfoService.getReplyById(id);
        if (reply != null) {
            return ResponseEntity.ok(reply);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
