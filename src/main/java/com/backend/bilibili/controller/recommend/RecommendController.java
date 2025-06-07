package com.backend.bilibili.controller.recommend;


import com.backend.bilibili.service.recommend.RecommendService;
import com.backend.bilibili.utils.UserTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    // 推荐视频
    @GetMapping("/video")
    public List<Long> recommendVideos(@RequestParam(defaultValue = "5") int topN) {
        Long userId = UserTokenUtil.getUid();
        return recommendService.recommendVideosForUser(userId, topN);
    }
}

