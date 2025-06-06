package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.profile.SpaceService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpaceServiceImpl implements SpaceService {
    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Override
    public List<VideoCardDTO> getUserPublishedVideos() {
        Long userId = UserTokenUtil.getUid();
        if (userId == null) {
            return Collections.emptyList();
        }

        // 直接查询 video_info 表中 author_id = userId 的视频列表
        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().eq("author_id", userId)
        );

        if (videos.isEmpty()) {
            return Collections.emptyList();
        }

        // 当前用户就是作者，所以只需构建 DTO，无需批量查询作者信息
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

            // 当前用户就是作者，直接写用户名（也可以从UserTokenUtil获取）
            dto.setAuthor(UserTokenUtil.getUsername());

            return dto;
        }).collect(Collectors.toList());
    }

}
