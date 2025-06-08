package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.profile.CollectionService;
import com.backend.bilibili.service.dto.util.VideoCardConvertUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Autowired
    private VideoCardConvertUtil videoCardConvertUtil;

    @Override
    public List<VideoCardDTO> getCollectionDetail(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }

        // 查询收藏记录
        List<UserCollect> collectList = userCollectMapper.selectList(
                new QueryWrapper<UserCollect>()
                        .eq("user_id", userId)
                        .select("video_id") // 只查 video_id 字段，提升性能
        );

        if (collectList == null || collectList.isEmpty()) {
            return Collections.emptyList();
        }

        // 提取视频ID列表
        List<Long> videoIds = collectList.stream()
                .map(UserCollect::getVideoId)
                .filter(Objects::nonNull)
                .distinct() // 避免重复 ID
                .collect(Collectors.toList());

        if (videoIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 批量查询视频信息
        List<VideoInfo> videos = videoInfoMapper.selectBatchIds(videoIds);

        if (videos == null || videos.isEmpty()) {
            return Collections.emptyList();
        }

        // 转换为DTO
        return videoCardConvertUtil.convertToCardDTOList(videos);
    }



}
