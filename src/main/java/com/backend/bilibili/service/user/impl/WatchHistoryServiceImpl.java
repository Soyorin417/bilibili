package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.mapper.user.WatchHistoryMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.WatchHistory;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.dto.util.VideoCardConvertUtil;
import com.backend.bilibili.service.user.account.WatchHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchHistoryServiceImpl implements WatchHistoryService {

    @Autowired
    private WatchHistoryMapper watchHistoryMapper;

    @Autowired
    private VideoInfoMapper videoInfoMapper;

    @Autowired
    private VideoCardConvertUtil videoCardConvertUtil;

    @Override
    public void recordWatch(Long userId, Long videoId) {
        if (userId == null || videoId == null) return;

        // 查询是否存在历史记录
        WatchHistory existing = watchHistoryMapper.selectOne(new QueryWrapper<WatchHistory>()
                .eq("user_id", userId)
                .eq("video_id", videoId));

        if (existing != null) {
            existing.setWatchTime(LocalDateTime.now());
            watchHistoryMapper.updateById(existing);
        } else {
            WatchHistory history = new WatchHistory();
            history.setUserId(userId);
            history.setVideoId(videoId);
            history.setWatchTime(LocalDateTime.now());
            watchHistoryMapper.insert(history);
        }
    }

    @Override
    public List<VideoCardDTO> getWatchHistory(Long userId) {
        List<WatchHistory> histories = watchHistoryMapper.selectList(
                new QueryWrapper<WatchHistory>()
                        .eq("user_id", userId)
                        .orderByDesc("watch_time")
        );

        List<Long> videoIds = histories.stream()
                .map(WatchHistory::getVideoId)
                .collect(Collectors.toList());

        List<VideoInfo> videos = videoInfoMapper.selectBatchIds(videoIds);
        return videoCardConvertUtil.convertToCardDTOList(videos);
    }
}
