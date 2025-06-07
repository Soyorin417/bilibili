package com.backend.bilibili.controller.user.account;


import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.user.account.UserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collect")
public class UserCollectController {

    @Autowired
    private UserCollectService userCollectService;

    @GetMapping("/list/{userId}")
    public List<VideoCardDTO> getUserCollectedVideos(@PathVariable Long userId) {
        return userCollectService.getUserCollectedVideos(userId);
    }
}