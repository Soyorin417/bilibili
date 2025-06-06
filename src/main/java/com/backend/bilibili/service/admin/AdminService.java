package com.backend.bilibili.service.admin;

import com.backend.bilibili.pojo.video.VideoInfo;

import java.util.List;

public interface AdminService {
    List<VideoInfo> getPendingVideos();
    boolean updateVideoStatus(Long id, String status);
}
