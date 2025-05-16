package com.backend.bilibili.controller.video;

import com.backend.bilibili.pojo.video.VideoActionInfo;
import com.backend.bilibili.service.video.VideoActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/video/action")
public class VideoActionController {

    @Autowired
    private VideoActionService videoActionService;

    @GetMapping("/status/{videoId}")
    public ResponseEntity<VideoActionInfo> getVideoActionStatus(@PathVariable Long videoId, @RequestParam Long userUid) {
        VideoActionInfo actionInfo = videoActionService.getUserVideoAction(videoId, userUid);
        return ResponseEntity.ok(actionInfo);
    }

    @PostMapping("/like/{videoId}")
    public ResponseEntity<String> like(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.likeVideo(videoId, userUid) ?
                ResponseEntity.ok("点赞成功") : ResponseEntity.badRequest().body("已点赞");
    }

    @DeleteMapping("/like/{videoId}")
    public ResponseEntity<String> cancelLike(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.cancelLike(videoId, userUid) ?
                ResponseEntity.ok("取消点赞成功") : ResponseEntity.badRequest().body("未点赞");
    }

    @PostMapping("/collect/{videoId}")
    public ResponseEntity<String> collect(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.collectVideo(videoId, userUid) ?
                ResponseEntity.ok("收藏成功") : ResponseEntity.badRequest().body("已收藏");
    }

    @DeleteMapping("/collect/{videoId}")
    public ResponseEntity<String> cancelCollect(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.cancelCollect(videoId, userUid) ?
                ResponseEntity.ok("取消收藏成功") : ResponseEntity.badRequest().body("未收藏");
    }

    @PostMapping("/coin/{videoId}")
    public ResponseEntity<String> coin(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.coinVideo(videoId, userUid) ?
                ResponseEntity.ok("投币成功") : ResponseEntity.badRequest().body("已投币");
    }

    @DeleteMapping("/coin/{videoId}")
    public ResponseEntity<String> cancelCoin(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.cancelCoin(videoId, userUid) ?
                ResponseEntity.ok("取消投币成功") : ResponseEntity.badRequest().body("未投币");
    }

    @PostMapping("/share/{videoId}")
    public ResponseEntity<String> share(@PathVariable Long videoId, @RequestParam Long userUid) {
        return videoActionService.shareVideo(videoId, userUid) ?
                ResponseEntity.ok("分享成功") : ResponseEntity.badRequest().body("分享失败");
    }

}
