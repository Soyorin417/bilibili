package com.backend.bilibili.controller.video;

import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.video.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RestController
public class VideoController {
    @Autowired
    private VideoInfoService videoInfoService;

    @GetMapping("/video/getAllVideo")
    public List<VideoInfo> getAllVideo() {
        return videoInfoService.getAllVideos();
    }

    @GetMapping("/video/getVideoById")
    public VideoInfo getVideoById(@RequestParam int id) {
        return videoInfoService.getVideoInfoById(id);
    }
}

