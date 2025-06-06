package com.backend.bilibili.controller.search;

import com.backend.bilibili.service.dto.UserDTO;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.search.impl.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // 搜索视频接口
    @GetMapping("/video")
    public List<VideoCardDTO> searchVideo(@RequestParam String keyword) {
        return searchService.searchVideo(keyword);
    }

    // 搜索用户接口
    @GetMapping("/user")
    public List<UserDTO> searchUser(@RequestParam String keyword, @RequestParam Long uid) {
        return searchService.searchUser(keyword, uid);
    }
}
