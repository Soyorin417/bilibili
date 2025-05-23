package com.backend.bilibili.service.video;

import com.backend.bilibili.pojo.video.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> getTagsByVideoId(Long videoId);
}
