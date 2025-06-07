package com.backend.bilibili.controller.search;

import com.backend.bilibili.pojo.search.UserSearchHistory;
import com.backend.bilibili.service.search.UserSearchHistoryService;
import com.backend.bilibili.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search/history")
public class UserSearchHistoryController {

    @Autowired
    private UserSearchHistoryService searchHistoryService;

    @PostMapping("/add")
    public ResponseEntity<Void> addSearch(@RequestParam String keyword) {
        Long userId = UserTokenUtil.getUid();
        searchHistoryService.addSearchHistory(userId, keyword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserSearchHistory>> getHistory() {
        Long userId = UserTokenUtil.getUid();
        return ResponseEntity.ok(searchHistoryService.getSearchHistory(userId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearHistory() {
        Long userId = UserTokenUtil.getUid();
        searchHistoryService.clearSearchHistory(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteOne(@RequestParam Long historyId) {
        Long userId = UserTokenUtil.getUid();
        searchHistoryService.deleteSearchHistory(userId, historyId);
        return ResponseEntity.ok().build();
    }
}
