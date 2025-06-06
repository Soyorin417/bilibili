package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.profile.CollectionService;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserCollectMapper userCollectMapper;

    @Override
    public List<VideoCardDTO> getCollectionDetail() {
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

        // 封装为 VideoCardDTO
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

            UserInfo author = authorMap.get(video.getAuthorId());
            dto.setAuthor(author != null ? author.getUsername() : "未知作者");

            return dto;
        }).collect(Collectors.toList());
    }

}
