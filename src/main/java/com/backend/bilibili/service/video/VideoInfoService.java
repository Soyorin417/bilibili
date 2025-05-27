package com.backend.bilibili.service.video;


import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoViewDTO;

import java.util.List;

public interface VideoInfoService {

    VideoViewDTO getVideoInfoById(int id);
    List<VideoViewDTO> getAllVideoViews();

    boolean submitVideo(VideoInfo videoInfo);    // 新增
    boolean deleteVideoById(int id);             // 删除
    boolean updateVideo(VideoInfo videoInfo);
    boolean increaseViewCount(Long videoId);

}
