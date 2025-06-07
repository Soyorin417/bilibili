package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.profile.SpaceService;
import com.backend.bilibili.service.user.utils.VideoCardConvertUtil;
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

    @Autowired
    private VideoCardConvertUtil videoCardConvertUtil;



    @Override
    public List<VideoCardDTO> getUserPublishedVideos() {
        Long userId = UserTokenUtil.getUid();
        if (userId == null) {
            return Collections.emptyList();
        }

        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().eq("author_id", userId)
        );

        if (videos.isEmpty()) {
            return Collections.emptyList();
        }

        List<VideoCardDTO> dtoList = videoCardConvertUtil.convertToCardDTOList(videos);

        String currentUsername = UserTokenUtil.getUsername();
        dtoList.forEach(dto -> dto.setAuthor(currentUsername));

        return dtoList;
    }

    @Override
    public List<VideoCardDTO> getUserPublishedVideos(long userId) {
        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().eq("author_id", userId)
        );

        if (videos.isEmpty()) {
            return Collections.emptyList();
        }

        return videoCardConvertUtil.convertToCardDTOList(videos);
    }



}
