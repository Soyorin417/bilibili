package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.video.TagMapper;
import com.backend.bilibili.mapper.video.VideoTagMapper;
import com.backend.bilibili.pojo.video.Tag;
import com.backend.bilibili.pojo.video.VideoTag;
import com.backend.bilibili.service.video.TagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public Tag createTag(Tag tag) {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", tag.getName());

        Tag existing = tagMapper.selectOne(queryWrapper);
        if (existing == null) {
            tagMapper.insert(tag);
            return tag;
        } else {
            return existing;
        }
    }



    @Override
    public boolean deleteTagById(Long tagId) {
        return tagMapper.deleteById(tagId) > 0;
    }

    @Override
    public boolean updateTag(Tag tag) {
        return tagMapper.updateById(tag) > 0;
    }

    @Override
    public Tag getTagById(Long tagId) {
        return tagMapper.selectById(tagId);
    }

    @Override
    public Tag getTagByName(String name){
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        return tagMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.selectList(null);
    }
}
