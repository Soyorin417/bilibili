package com.backend.bilibili.service.search.impl;

import com.backend.bilibili.mapper.user.UserFollowMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserFollow;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.UserDTO;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.search.SearchService;
import com.backend.bilibili.service.user.account.InfoService;
import com.backend.bilibili.service.user.utils.VideoCardConvertUtil;
import com.backend.bilibili.service.video.VideoInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserFollowMapper userFollowMapper;

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private VideoInfoService videoInfoService;

    @Autowired
    private InfoService infoService;


    @Autowired
    private VideoCardConvertUtil videoCardConvertUtil;

    @Override
    public List<VideoCardDTO> searchVideo(String keyword) {
        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().like("title", keyword)
        );

        return videoCardConvertUtil.convertToCardDTOList(videos);
    }



    public List<UserDTO> searchUser(String keyword, Long currentUid) {
        // 模糊搜索用户
        List<UserInfo> users = userInfoMapper.selectList(
                new QueryWrapper<UserInfo>().like("username", keyword)
        );

        // 查询当前用户已关注的人 ID 列表
        Set<Long> followedIds = userFollowMapper.selectList(
                new QueryWrapper<UserFollow>()
                        .eq("follower_uid", currentUid)
                        .eq("status", 1)
        ).stream().map(UserFollow::getFollowingUid).collect(Collectors.toSet());

        // 构建返回结果
        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            Long targetUid = user.getUid();  // ⬅ 目标用户 ID

            dto.setId(targetUid);
            dto.setUsername(user.getUsername());
            dto.setAvatar(user.getAvatar());
            dto.setLevel(user.getLevel());
            dto.setFollow(followedIds.contains(targetUid));

            Long fansCount = infoService.getUserFansCount(currentUid);
            dto.setFans(fansCount);

            Long videoCount = videoInfoService.getVideoCountByUserId(targetUid);
            dto.setVideoCount(videoCount);

            return dto;
        }).collect(Collectors.toList());
    }



}
