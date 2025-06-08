package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.user.account.WatchHistoryService;
import com.backend.bilibili.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watch-history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    @PostMapping("/record")
    public ResponseEntity<?> recordWatch(@RequestParam Long videoId) {
        Long userId = UserTokenUtil.getUid();
        watchHistoryService.recordWatch(userId, videoId);
        return ResponseEntity.ok("已记录观看历史");
    }

    @GetMapping()
    public ResponseEntity<List<VideoCardDTO>> getWatchHistory() {
        Long userId = UserTokenUtil.getUid();
        List<VideoCardDTO> history = watchHistoryService.getWatchHistory(userId);
        return ResponseEntity.ok(history);
    }
}
