package com.backend.bilibili.service.video.impl;


import com.backend.bilibili.mapper.video.TagMapper;
import com.backend.bilibili.mapper.video.VideoTagMapper;
import com.backend.bilibili.pojo.video.Tag;
import com.backend.bilibili.pojo.video.VideoTag;
import com.backend.bilibili.service.video.TagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {


    @Autowired
    private VideoTagMapper videoTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<Tag> getTagsByVideoId(Long videoId) {
        List<Long> tagIds = videoTagMapper.selectList(
                        new QueryWrapper<VideoTag>().eq("video_id", videoId))
                .stream()
                .map(VideoTag::getTagId)
                .collect(Collectors.toList());

        if (tagIds.isEmpty()) {
            return Collections.emptyList();
        }


        return tagMapper.selectList(new QueryWrapper<Tag>().in("id", tagIds));
    }
}
