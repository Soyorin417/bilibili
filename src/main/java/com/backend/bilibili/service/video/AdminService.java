package com.backend.bilibili.service.video;

import com.backend.bilibili.pojo.video.VideoInfo;

import java.util.List;

public interface AdminService {
    List<VideoInfo> getPendingVideos();
    boolean updateVideoStatus(Long id, String status);
}
