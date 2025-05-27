package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.service.dto.UserCollectVideoDTO;
import com.backend.bilibili.service.user.account.UserCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collect")
public class UserCollectController {

    @Autowired
    private UserCollectService userCollectService;

    @GetMapping("/list/{userId}")
    public List<UserCollectVideoDTO> getUserCollectedVideos(@PathVariable Long userId) {
        return userCollectService.getUserCollectedVideos(userId);
    }


}
