package com.backend.bilibili.service.video;


import com.backend.bilibili.pojo.video.VideoInfo;

import java.util.List;

public interface VideoInfoService {

    VideoInfo getVideoInfoById(int id);
    List<VideoInfo> getAllVideos();
}
