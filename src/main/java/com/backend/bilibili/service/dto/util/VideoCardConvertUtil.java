package com.backend.bilibili.service.dto.util;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.video.comment.CommentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class VideoCardConvertUtil {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CommentInfoService commentInfoService;

    public List<VideoCardDTO> convertToCardDTOList(List<VideoInfo> videoList) {
        if (videoList == null || videoList.isEmpty()) return Collections.emptyList();

        Set<Long> authorIds = videoList.stream()
                .map(VideoInfo::getAuthorId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, UserInfo> authorMap = userInfoMapper.selectBatchIds(authorIds)
                .stream().collect(Collectors.toMap(UserInfo::getUid, u -> u));

        return videoList.stream().map(video -> {
            UserInfo author = authorMap.get(video.getAuthorId());
            int commentCount = commentInfoService.getCommentCountByVideoId((long) video.getId());

            return new VideoCardDTO(
                    (long) video.getId(),
                    video.getTitle(),
                    video.getViews(),
                    commentCount,
                    video.getTime(),
                    video.getDescription(),
                    video.getVideoUrl(),
                    video.getImage(),
                    video.getDuration(),
                    author != null ? author.getUsername() : "未知作者"
            );
        }).collect(Collectors.toList());
    }
}
