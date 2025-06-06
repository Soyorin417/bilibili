package com.backend.bilibili.controller.profile;

import com.backend.bilibili.service.profile.SpaceService;
import com.backend.bilibili.service.dto.VideoCardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/space")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    // 获取当前用户发布的视频列表
    @GetMapping("/videos")
    public List<VideoCardDTO> getUserPublishedVideos() {
        return spaceService.getUserPublishedVideos();
    }
}
