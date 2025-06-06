package com.backend.bilibili.service.video;

import com.backend.bilibili.pojo.video.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> getTagsByVideoId(Long videoId);
    Tag createTag(Tag tag);
    boolean updateTag(Tag tag);
    boolean deleteTagById(Long tagId);
    Tag getTagByName(String tagName);
    Tag getTagById(Long tagId);
    List<Tag> getAllTags();
}
