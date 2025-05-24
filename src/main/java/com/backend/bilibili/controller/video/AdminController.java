package com.backend.bilibili.controller.video;

import com.backend.bilibili.pojo.video.VideoInfo;

import com.backend.bilibili.service.video.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/videos")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/pending")
    public List<VideoInfo> getPendingVideos() {
        return adminService.getPendingVideos();
    }

    @PostMapping("/review")
    public String reviewVideo(@RequestBody Map<String, Object> payload) {
        Long id = Long.valueOf(payload.get("id").toString());
        String status = payload.get("status").toString();

        boolean result = adminService.updateVideoStatus(id, status);
        return result ? "success" : "fail";
    }
}
