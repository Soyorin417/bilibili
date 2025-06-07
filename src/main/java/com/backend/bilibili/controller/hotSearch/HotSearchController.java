package com.backend.bilibili.controller.hotSearch;

import com.backend.bilibili.service.hotSearch.HotSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hot")
public class HotSearchController {

    @Autowired
    private HotSearchService hotSearchService;

    @PostMapping("/add")
    public ResponseEntity<Void> incrementSearch(@RequestBody Map<String, String> body) {

        System.out.println(body + "还是有请求的");
        String keyword = body.get("keyword");
        if (keyword == null || keyword.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        hotSearchService.incrementSearchCount(keyword.trim());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/top")
    public ResponseEntity<List<String>> getTopHotSearches(@RequestParam(defaultValue = "10") int limit) {
        List<String> hotKeywords = hotSearchService.getTopHotSearches(limit);
        return ResponseEntity.ok(hotKeywords);
    }
}
