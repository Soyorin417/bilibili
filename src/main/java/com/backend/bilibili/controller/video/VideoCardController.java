package com.backend.bilibili.controller.video;

import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.video.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/video/cards")
public class VideoCardController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public List<VideoCardDTO> getVideoCards() {
        return videoService.getVideoCardList();
    }
}
