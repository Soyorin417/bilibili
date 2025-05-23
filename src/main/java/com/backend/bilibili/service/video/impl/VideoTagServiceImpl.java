package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.video.VideoTagMapper;
import com.backend.bilibili.pojo.video.VideoTag;
import com.backend.bilibili.service.video.VideoTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag> implements VideoTagService {

    @Override
    public void addTagToVideo(Long videoId, Long tagId) {
        VideoTag vt = new VideoTag();
        vt.setVideoId(videoId);
        vt.setTagId(tagId);
        this.save(vt);
    }

    @Override
    public void removeTagFromVideo(Long videoId, Long tagId) {
        lambdaUpdate()
                .eq(VideoTag::getVideoId, videoId)
                .eq(VideoTag::getTagId, tagId)
                .remove();
    }
}

