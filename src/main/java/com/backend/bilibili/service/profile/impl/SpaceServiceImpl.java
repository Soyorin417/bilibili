package com.backend.bilibili.service.profile.impl;

import com.backend.bilibili.mapper.user.UserCollectMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.profile.SpaceService;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.dto.util.VideoCardConvertUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

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

    @Autowired
    private InfoService infoService;

    @Override
    public List<VideoCardDTO> getUserPublishedVideos(long userId) {
        if (userId == 0L) { // 或者判断其他非法值
            return Collections.emptyList();
        }

        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().eq("author_id", userId)
        );

        if (videos.isEmpty()) {
            return Collections.emptyList();
        }

        List<VideoCardDTO> dtoList = videoCardConvertUtil.convertToCardDTOList(videos);

        // 这里要根据 userId 查询用户名，不要用 UserTokenUtil，token里是当前登录用户，不一定是请求的userId
        String username = infoService.getUserNameById(userId);
        dtoList.forEach(dto -> dto.setAuthor(username));

        return dtoList;
    }


}
