package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.video.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public List<VideoCardDTO> getVideoCardList() {
        QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "approved"); // 只查已审核通过的视频
        List<VideoInfo> videos = videoInfoMapper.selectList(queryWrapper);

        Set<Long> authorIds = videos.stream()
                .map(VideoInfo::getAuthorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.in("uid", authorIds);
        List<UserInfo> authors = userInfoMapper.selectList(wrapper);

        Map<Long, String> authorMap = new HashMap<>();
        for (UserInfo author : authors) {
            authorMap.put(author.getUid(), author.getUsername());
        }

        System.out.println(authorMap);
        return videos.stream().map(video -> {
            VideoCardDTO dto = new VideoCardDTO();
            dto.setId((long) video.getId());
            dto.setTitle(video.getTitle());
            dto.setViews(video.getViews());
            dto.setComments(video.getComments());
            dto.setTime(video.getTime());
            dto.setDescription(video.getDescription());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setImage(video.getImage());
            dto.setDuration(video.getDuration());
            dto.setAuthor(authorMap.getOrDefault(video.getAuthorId(), "未知作者"));
            return dto;
        }).collect(Collectors.toList());
    }

}
