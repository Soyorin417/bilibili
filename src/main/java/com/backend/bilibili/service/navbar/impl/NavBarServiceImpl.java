package com.backend.bilibili.service.navbar.impl;

import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserHistoryMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.pojo.user.UserHistory;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.NavBarDTO;
import com.backend.bilibili.service.navbar.NavBarService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service

public class NavBarServiceImpl implements NavBarService {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Autowired
    private UserHistoryMapper userHistoryMapper;

    @Override
    public List<NavBarDTO> getHistory(){
        Long userId = UserTokenUtil.getUid();
        if(userId == null){
            return null;
        }

        List<UserHistory> historyList = userHistoryMapper.selectList(new QueryWrapper<UserHistory>().eq("user_id", userId));
        if(historyList == null){
            return null;
        }

        List<Long> videoIds = historyList.stream().map(UserHistory::getVideoId).collect(Collectors.toList());
        List<VideoInfo> videos = videoInfoMapper.selectBatchIds(videoIds);

        Set<Long> authorIds = videos.stream()
                .map(VideoInfo::getAuthorId)
                .collect(Collectors.toSet());

        List<UserInfo> authors = userInfoMapper.selectBatchIds(authorIds);
        Map<Long, UserInfo> authorMap = authors.stream()
                .collect(Collectors.toMap(UserInfo::getUid, u -> u));

        // 封装为 NavBarDTO
        return videos.stream().map(video -> {
            NavBarDTO dto = new NavBarDTO();
            dto.setId(video.getId());
            dto.setTitle(video.getTitle());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setImage(video.getImage());
            dto.setCreateTime(video.getTime().toString());

            UserInfo author = authorMap.get(video.getAuthorId());
            if (author != null) {
                dto.setUsername(author.getUsername());
                dto.setAvatar(author.getAvatar());
            }
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public List<NavBarDTO> getCollection() {
        Long userId = UserTokenUtil.getUid();
        if (userId == null) {
            return Collections.emptyList();
        }

        // 获取用户收藏的视频ID列表
        List<UserCollect> collectList = userCollectMapper.selectList(
                new QueryWrapper<UserCollect>().eq("user_id", userId)
        );
        if (collectList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> videoIds = collectList.stream()
                .map(UserCollect::getVideoId)
                .collect(Collectors.toList());

        List<VideoInfo> videos = videoInfoMapper.selectBatchIds(videoIds);

        // 批量查询作者信息
        Set<Long> authorIds = videos.stream()
                .map(VideoInfo::getAuthorId)
                .collect(Collectors.toSet());

        List<UserInfo> authors = userInfoMapper.selectBatchIds(authorIds);
        Map<Long, UserInfo> authorMap = authors.stream()
                .collect(Collectors.toMap(UserInfo::getUid, u -> u));

        // 封装为 NavBarDTO
        return videos.stream().map(video -> {
            NavBarDTO dto = new NavBarDTO();
            dto.setId(video.getId());
            dto.setTitle(video.getTitle());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setImage(video.getImage());
            dto.setCreateTime(video.getTime().toString());

            UserInfo author = authorMap.get(video.getAuthorId());
            if (author != null) {
                dto.setUsername(author.getUsername());
                dto.setAvatar(author.getAvatar());
            }
            return dto;
        }).collect(Collectors.toList());
    }


    public List<NavBarDTO> getRightActivities() {
        // 查询 video_info 表中 show_right = 1 的视频
        QueryWrapper<VideoInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("show_right", 1)
                .eq("status", "approved")  // 可选：只取审核通过的视频
                .orderByDesc("time")
                .last("LIMIT 10");

        List<VideoInfo> videoList = videoInfoMapper.selectList(wrapper);

        // 映射为 NavBarActivityDTO
        return videoList.stream().map(video -> {
            UserInfo user = userInfoMapper.selectOne(
                    new QueryWrapper<UserInfo>().eq("uid", video.getAuthorId())
            );

            return new NavBarDTO(
                    video.getId(),
                    video.getTitle(),
                    user != null ? user.getUsername() : "",
                    user != null ? user.getAvatar() : "",
                    video.getTime() != null ? video.getTime().toString() : "",
                    video.getImage(),
                    video.getVideoUrl()
            );
        }).collect(Collectors.toList());
    }
}
