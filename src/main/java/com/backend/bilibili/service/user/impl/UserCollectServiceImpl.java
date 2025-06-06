package com.backend.bilibili.service.user.impl;


import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserCollect;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.UserCollectVideoDTO;
import com.backend.bilibili.service.user.account.UserCollectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserCollectServiceImpl extends ServiceImpl<UserCollectMapper, UserCollect> implements UserCollectService {

    @Autowired
    VideoInfoMapper videoInfoMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    UserCollectMapper userCollectMapper;


    @Override
    public List<UserCollect> getUserCollects(Long userId) {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).orderByDesc("collect_time");
        return list(queryWrapper);
    }
    @Override
    public UserCollect getOne(Long userId, Long videoId) {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("video_id", videoId);
        return baseMapper.selectOne(queryWrapper);
    }

    // 添加收藏
    @Override
    public boolean addCollect(Long userId, Long videoId) {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("video_id", videoId);
        if (baseMapper.selectCount(queryWrapper) > 0) {
            return false;
        }
        UserCollect userCollect = new UserCollect();
        userCollect.setUserId(userId);
        userCollect.setVideoId(videoId);
        userCollect.setCollectTime(new Date());
        return save(userCollect);
    }


    // 删除收藏
    @Override
    public boolean deleteCollect(Long userId, Long videoId) {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("video_id", videoId);
        return remove(queryWrapper);
    }

    // 更新收藏（更新时间或其他字段）
    @Override
    public boolean updateCollect(UserCollect collect) {
        return updateById(collect);
    }

    @Override
    public List<UserCollectVideoDTO> getUserCollectedVideos(Long userId) {
        // 1. 查询用户收藏的视频ID
        List<Long> videoIds = userCollectMapper.selectList(
                        new QueryWrapper<UserCollect>().eq("user_id", userId)
                ).stream()
                .map(UserCollect::getVideoId)
                .collect(Collectors.toList());

        if (videoIds.isEmpty()) return Collections.emptyList();

        // 2. 查询视频信息
        List<VideoInfo> videos = videoInfoMapper.selectBatchIds(videoIds);

        // 3. 批量查询作者信息并映射
        Map<Long, UserInfo> authorMap = userInfoMapper.selectBatchIds(
                videos.stream().map(VideoInfo::getAuthorId).collect(Collectors.toSet())
        ).stream().collect(Collectors.toMap(UserInfo::getUid, a -> a));

        // 4. 封装为 DTO
        return videos.stream().map(video -> {
            UserInfo author = authorMap.get(video.getAuthorId());
            return new UserCollectVideoDTO(
                    (long) video.getId(),
                    video.getTitle(),
                    video.getImage(),
                    video.getVideoUrl(),
                    video.getViews(),
                    video.getComments(),
                    video.getDuration()
            );
        }).collect(Collectors.toList());

    }

}