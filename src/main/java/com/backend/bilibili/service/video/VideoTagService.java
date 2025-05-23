package com.backend.bilibili.service.video;

import com.backend.bilibili.pojo.video.VideoTag;
import com.baomidou.mybatisplus.extension.service.IService;

public interface VideoTagService extends IService<VideoTag> {
    void addTagToVideo(Long videoId, Long tagId);
    void removeTagFromVideo(Long videoId, Long tagId);
}
