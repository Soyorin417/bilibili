package com.backend.bilibili.service.user.account;

import com.backend.bilibili.service.dto.VideoCardDTO;

import java.util.List;

public interface WatchHistoryService {
    void recordWatch(Long userId, Long videoId);
    List<VideoCardDTO> getWatchHistory(Long userId);
}