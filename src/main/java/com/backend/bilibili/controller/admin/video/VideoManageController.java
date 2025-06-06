package com.backend.bilibili.controller.admin.video;

import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.admin.AdminService;
import com.backend.bilibili.service.video.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/admin")
public class VideoManageController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private VideoInfoService videoInfoService;

    @GetMapping("/video/getUserCount")
    public Long getVideoCount(){
        return videoInfoService.getVideoCount();
    }

    @GetMapping("/videos/pending")
    public List<VideoInfo> getPendingVideos() {
        return adminService.getPendingVideos();
    }

    @PostMapping("/videos/review")
    public String reviewVideo(@RequestBody Map<String, Object> payload) {
        Long id = Long.valueOf(payload.get("id").toString());
        String status = payload.get("status").toString();

        boolean result = adminService.updateVideoStatus(id, status);
        return result ? "success" : "fail";
    }
}
