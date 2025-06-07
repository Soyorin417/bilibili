package com.backend.bilibili.service.video;


import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.UploadVideoDTO;
import com.backend.bilibili.service.dto.VideoViewDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VideoInfoService {

    VideoViewDTO getVideoInfoById(int id);
    List<VideoViewDTO> getAllVideoViews();
    List<VideoViewDTO> recommendVideos();
    List<VideoViewDTO> getAllVideoViewsByUserId(int userId);
    ResponseEntity<Map<String, Object>> submitVideoWithParams(String title, String videoFileName,
                                                              String coverFileName, String description,
                                                              String duration, Long authorId,String danmakuFileName);

    boolean submitVideo(VideoInfo videoInfo);    // 新增
    boolean deleteVideoById(int id);             // 删除
    boolean updateVideo(VideoInfo videoInfo);
    boolean increaseViewCount(Long videoId);
    Long getVideoCount();
    Long  getVideoCountByUserId(Long userId);
    ResponseEntity<?> submitVideoTest(UploadVideoDTO dto);

}
