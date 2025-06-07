package com.backend.bilibili.service.video.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.video.VideoService;
import com.backend.bilibili.service.video.comment.CommentInfoService;
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

    @Autowired
    private CommentInfoService commentInfoService;


    @Override
    public List<VideoCardDTO> getVideoCardList() {
        // 查询已审核通过的视频
        QueryWrapper<VideoInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "approved");
        List<VideoInfo> videos = videoInfoMapper.selectList(queryWrapper);

        // 提取作者ID集合
        Set<Long> authorIds = videos.stream()
                .map(VideoInfo::getAuthorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 查询作者信息
        Map<Long, String> authorMap = new HashMap<>();
        if (!authorIds.isEmpty()) {
            QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
            wrapper.in("uid", authorIds);
            List<UserInfo> authors = userInfoMapper.selectList(wrapper);
            for (UserInfo author : authors) {
                authorMap.put(author.getUid(), author.getUsername());
            }
        }

        // 构建视频卡片列表
        return videos.stream().map(video -> {
            VideoCardDTO dto = new VideoCardDTO();
            dto.setId((long) video.getId());
            dto.setTitle(video.getTitle());
            dto.setViews(video.getViews());

            // 评论数实时统计（包含回复）
            int commentCount = commentInfoService.getCommentCountByVideoId((long) video.getId());
            dto.setComments(commentCount);

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
