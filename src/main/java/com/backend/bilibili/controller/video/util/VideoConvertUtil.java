package com.backend.bilibili.controller.video.util;

import com.backend.bilibili.mapper.user.UserInfoMapper;
import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoViewDTO;
import com.backend.bilibili.service.video.comment.CommentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VideoConvertUtil {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private CommentInfoService commentInfoService;

    /**
     * 将 VideoInfo 列表转换为带作者头像和粉丝数的 VideoViewDTO 列表
     */
    public List<VideoViewDTO> convertToDTOList(List<VideoInfo> videoList) {
        List<VideoViewDTO> dtoList = new ArrayList<>();
        for (VideoInfo video : videoList) {
            dtoList.add(convertToDTO(video));
        }
        return dtoList;
    }

    /**
     * 转换单个 VideoInfo 到 DTO
     */
    public VideoViewDTO convertToDTO(VideoInfo video) {
        VideoViewDTO dto = new VideoViewDTO();
        dto.setId(video.getId());
        dto.setTitle(video.getTitle());
        dto.setViews(video.getViews());

        int commentCount = commentInfoService.getCommentCountByVideoId((long) video.getId());
        dto.setComments(commentCount);

        dto.setTime(video.getTime());
        dto.setDescription(video.getDescription());
        dto.setVideoUrl(video.getVideoUrl());
        dto.setImage(video.getImage());
        dto.setShowRight(video.isShowRight());
        dto.setAuthorId(video.getAuthorId());
        dto.setDuration(video.getDuration());
        dto.setLikeCount(video.getLikeCount());
        dto.setCollectCount(video.getCollectCount());
        dto.setCoinCount(video.getCoinCount());
        dto.setShareCount(video.getShareCount());
        dto.setStatus(video.getStatus());

        // 作者信息
        if (video.getAuthorId() != null) {
            QueryWrapper<UserInfo> authorQueryWrapper = new QueryWrapper<>();
            authorQueryWrapper.eq("uid", video.getAuthorId());
            UserInfo author = userInfoMapper.selectOne(authorQueryWrapper);
            if (author != null) {
                dto.setAvatar(author.getAvatar());
                dto.setFollow(String.valueOf(author.getFans()));
                dto.setAuthorName(author.getUsername());
            }
        }

        return dto;
    }
}
