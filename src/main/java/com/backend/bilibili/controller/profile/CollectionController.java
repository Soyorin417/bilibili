package com.backend.bilibili.controller.profile;

import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.profile.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collect")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    // 详细收藏列表
    @GetMapping("/detail")
    public List<VideoCardDTO> getCollectionDetail(@RequestParam Long userId) {
        return collectionService.getCollectionDetail(userId);
    }
}
