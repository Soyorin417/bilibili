package com.backend.bilibili.controller.video;

import com.backend.bilibili.service.video.VideoTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/video-tags")
public class VideoTagController {

    @Autowired
    private VideoTagService videoTagService;

    // 给视频添加标签
    @PostMapping("/add")
    public String addTagToVideo(@RequestParam Long videoId, @RequestParam Long tagId) {
        videoTagService.addTagToVideo(videoId, tagId);
        return "标签添加成功";
    }

    // 从视频移除标签
    @PostMapping("/remove")
    public String removeTagFromVideo(@RequestParam Long videoId, @RequestParam Long tagId) {
        videoTagService.removeTagFromVideo(videoId, tagId);
        return "标签移除成功";
    }
}
