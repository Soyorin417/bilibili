package com.backend.bilibili.service.navbar.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.NavBarActivityDTO;
import com.backend.bilibili.service.navbar.NavBarService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class NavBarServiceImpl implements NavBarService {

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    public List<NavBarActivityDTO> getRightActivities() {
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

            return new NavBarActivityDTO(
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
