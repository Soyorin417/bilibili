package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.video.VideoTagMapper;
import com.backend.bilibili.pojo.video.VideoTag;
import com.backend.bilibili.service.video.VideoTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Long> getTagIdsByVideoId(Long videoId) {
        QueryWrapper<VideoTag> wrapper = new QueryWrapper<>();
        wrapper.eq("video_id", videoId).select("tag_id");
        List<VideoTag> list = this.list(wrapper);
        return list.stream()
                .map(VideoTag::getTagId)
                .collect(Collectors.toList());
    }
}

