package com.backend.bilibili.service.user.account;

import com.backend.bilibili.service.dto.VideoCardDTO;

import java.util.List;

public interface WatchHistoryService {

    void recordWatch(Long userId, Long videoId);

    List<VideoCardDTO> getWatchHistory(Long userId);

    boolean deleteWatchHistory(Long userId, Long videoId);

    void clearAllHistory(Long userId);

    void updateWatchTime(Long userId, Long videoId);
}