package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDetailDTO;
import com.backend.bilibili.service.profile.DynamicService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DynamicServiceImpl implements DynamicService {

    @Autowired
    VideoInfoMapper videoInfoMapper;

    @Autowired
    UserInfoMapper userInfoMapper;


    @Override
    public List<VideoCardDetailDTO> getUserDynamics() {
        Long userId = UserTokenUtil.getUid();
        if (userId == null) {
            return Collections.emptyList();
        }

        // 获取当前用户发布的视频
        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().eq("author_id", userId)
        );

        // 获取当前用户信息（只查一条）
        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            return Collections.emptyList();
        }

        // 封装 DTO 列表
        return videos.stream().map(video -> {
            VideoCardDetailDTO dto = new VideoCardDetailDTO();
            dto.setId(video.getId());
            dto.setTitle(video.getTitle());
            dto.setViews(video.getViews());
            dto.setComments(video.getComments());
            dto.setTime(video.getTime());
            dto.setDescription(video.getDescription());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setImage(video.getImage());
            dto.setDuration(video.getDuration());
            dto.setLikeCount(video.getLikeCount());
            dto.setShareCount(video.getShareCount());

            // 使用 UserInfo 的真实数据
            dto.setAuthorName(user.getUsername());
            dto.setAvatar(user.getAvatar());

            return dto;
        }).collect(Collectors.toList());
    }



}
