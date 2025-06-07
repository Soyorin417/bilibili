package com.backend.bilibili.service.recommend;

import com.backend.bilibili.pojo.video.VideoInfo;
import java.util.List;

public interface RecommendService  {
    List<Long> recommendTagsForUser(Long userId, int topN);
    List<Long> recommendVideosForUser(Long userId, int topN);
}

