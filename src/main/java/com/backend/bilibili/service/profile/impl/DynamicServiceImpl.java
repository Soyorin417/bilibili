package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDetailDTO;
import com.backend.bilibili.service.profile.DynamicService;
import com.backend.bilibili.service.dto.util.VideoCardDetailConvertUtil;
import com.backend.bilibili.utils.UserTokenUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class DynamicServiceImpl implements DynamicService {

    @Autowired
    VideoInfoMapper videoInfoMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public List<VideoCardDetailDTO> getUserDynamics(Long userId) {
        if (userId == null) {
            userId = UserTokenUtil.getUid();
        }
        if (userId == null) {
            return Collections.emptyList();
        }

        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().eq("author_id", userId)
        );

        UserInfo user = userInfoMapper.selectById(userId);
        if (user == null) {
            return Collections.emptyList();
        }

        return VideoCardDetailConvertUtil.convertToDetailDTOList(videos, user);
    }


}
