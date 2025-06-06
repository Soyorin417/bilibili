package com.backend.bilibili.controller.video;

import com.backend.bilibili.pojo.video.Tag;
import com.backend.bilibili.service.video.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagService tagService;


    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.list();
    }

    // 根据视频ID获取标签
    @GetMapping("/video/{videoId}")
    public List<Tag> getTagsByVideoId(@PathVariable Long videoId) {
        return tagService.getTagsByVideoId(videoId);
    }

    // 添加标签
    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        Tag savedTag = tagService.createTag(tag); // 返回带id的Tag对象
        return savedTag;
    }

    // 更新标签
    @PutMapping
    public boolean updateTag(@RequestBody Tag tag) {
        return tagService.updateTag(tag);
    }

    // 删除标签
    @DeleteMapping("/{id}")
    public boolean deleteTag(@PathVariable Long id) {
        return tagService.deleteTagById(id);
    }
}

