package com.backend.bilibili.controller.video.comment;

import com.backend.bilibili.pojo.video.comment.ReplyInfo;
import com.backend.bilibili.service.dto.ReplyDTO;
import com.backend.bilibili.service.video.comment.ReplyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reply")
public class ReplyInfoController {

    @Autowired
    private ReplyInfoService replyInfoService;

    // 获取某条评论下的所有回复
    @GetMapping("/list")
    public ResponseEntity<List<ReplyDTO>> getReplies(@RequestParam Long commentId) {
        List<ReplyDTO> replies = replyInfoService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }

    // 新增一条回复
    @PostMapping("/add")
    public ResponseEntity<String> addReply(@RequestBody Map<String, Object> data) {
        try {
            // 参数校验
            if (!data.containsKey("commentId") || !data.containsKey("content")) {
                return ResponseEntity.badRequest().body("参数缺失");
            }

            Long commentId = Long.valueOf(data.get("commentId").toString());
            String content = data.get("content").toString();

            replyInfoService.addReply(commentId, content);
            return ResponseEntity.ok("回复添加成功");

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("添加失败");
        }
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReply(@PathVariable Long id, @RequestBody ReplyDTO replyDTO) {
        if (!id.equals(replyDTO.getId())) {
            System.out.println("路径id和请求体id不一致");
            return ResponseEntity.badRequest().body("路径ID与实体ID不一致");
        }

        boolean success = replyInfoService.updateReplyById(id, replyDTO);
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
