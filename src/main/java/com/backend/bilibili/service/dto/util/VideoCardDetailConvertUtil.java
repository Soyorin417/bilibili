package com.backend.bilibili.service.dto.util;

import com.backend.bilibili.pojo.user.UserInfo;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDetailDTO;

import java.util.ArrayList;
import java.util.List;

public class VideoCardDetailConvertUtil {
    public static List<VideoCardDetailDTO> convertToDetailDTOList(List<VideoInfo> videos, UserInfo user) {
        if (videos == null || user == null) {
            return new ArrayList<>();
        }

        List<VideoCardDetailDTO> dtoList = new ArrayList<>();
        for (VideoInfo video : videos) {
            VideoCardDetailDTO dto = new VideoCardDetailDTO();
            dto.setId(video.getId());
            dto.setTitle(video.getTitle());
            dto.setViews(video.getViews());
            dto.setComments(video.getComments());
            dto.setTime(video.getTime());
            dto.setDescription(video.getDescription());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setImage(video.getImage());
            dto.setDuration(video.getDuration());
            dto.setLikeCount(video.getLikeCount());
            dto.setShareCount(video.getShareCount());
            dto.setAuthorName(user.getUsername());
            dto.setAvatar(user.getAvatar());
            dtoList.add(dto);
        }
        return dtoList;
    }
}
