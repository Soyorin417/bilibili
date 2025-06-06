package com.backend.bilibili.service.search;

import com.backend.bilibili.mapper.user.UserFollowMapper;
import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.UserFollow;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.UserDTO;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.search.impl.SearchService;
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


    public List<VideoCardDTO> searchVideo(String keyword) {
        List<VideoInfo> videos = videoInfoMapper.selectList(
                new QueryWrapper<VideoInfo>().like("title", keyword)
        );

        return videos.stream().map(video -> {
            UserInfo author = userInfoMapper.selectById(video.getAuthorId());
            return new VideoCardDTO(
                    (long) video.getId(),
                    video.getTitle(),
                    video.getViews(),
                    video.getComments(),
                    video.getTime(),
                    video.getDescription(),
                    video.getVideoUrl(),
                    video.getImage(),
                    video.getDuration(),
                    author != null ? author.getUsername() : "未知作者"
            );
        }).collect(Collectors.toList());
    }


    public List<UserDTO> searchUser(String keyword, Long currentUid) {
        List<UserInfo> users = userInfoMapper.selectList(
                new QueryWrapper<UserInfo>().like("username", keyword)
        );

        Set<Long> followedIds = userFollowMapper.selectList(
                new QueryWrapper<UserFollow>()
                        .eq("follower_uid", currentUid)
                        .eq("status", 1)
        ).stream().map(UserFollow::getFollowingUid).collect(Collectors.toSet());

        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getUid());
            dto.setUsername(user.getUsername());
            dto.setAvatar(user.getAvatar());
            dto.setLevel(user.getLevel());
            dto.setFollow(followedIds.contains(user.getUid()));
            return dto;
        }).collect(Collectors.toList());
    }


}
