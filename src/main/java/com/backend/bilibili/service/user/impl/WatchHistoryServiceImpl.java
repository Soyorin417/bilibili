package com.backend.bilibili.service.user.impl;

import com.backend.bilibili.mapper.user.WatchHistoryMapper;
import com.backend.bilibili.mapper.video.VideoInfoMapper;
import com.backend.bilibili.pojo.user.WatchHistory;
import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoCardDTO;
import com.backend.bilibili.service.dto.util.VideoCardConvertUtil;
import com.backend.bilibili.service.recommend.UserTagScoreService;
import com.backend.bilibili.service.recommend.UserVideoScoreService;
import com.backend.bilibili.service.user.account.UserCollectService;
import com.backend.bilibili.service.user.account.WatchHistoryService;
import com.backend.bilibili.service.video.VideoTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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

    @Autowired
    private UserVideoScoreService userVideoScoreService;

    @Autowired
    private VideoTagService videoTagService;

    private UserTagScoreService userTagScoreService;


    @Override
    public void recordWatch(Long userId, Long videoId) {
        if (userId == null || videoId == null) return;

        WatchHistory exist = watchHistoryMapper.selectOne(
                new QueryWrapper<WatchHistory>().eq("user_id", userId).eq("video_id", videoId));

        if (exist != null) {
            exist.setWatchTime(LocalDateTime.now());
            watchHistoryMapper.updateById(exist);
        } else {
            WatchHistory history = new WatchHistory();
            history.setUserId(userId);
            history.setVideoId(videoId);
            history.setWatchTime(LocalDateTime.now());
            watchHistoryMapper.insert(history);
        }
        userVideoScoreService.saveOrUpdateScore(userId, videoId, "watch");

        List<Long> tagIds = videoTagService.getTagIdsByVideoId(videoId);
        for (Long tagId : tagIds) {
            userTagScoreService.saveOrUpdateScore(userId, tagId, "watch");
        }


    }

    @Override
    public void updateWatchTime(Long userId, Long videoId) {
        if (userId == null || videoId == null) return;

        WatchHistory exist = watchHistoryMapper.selectOne(
                new QueryWrapper<WatchHistory>().eq("user_id", userId).eq("video_id", videoId));
        if (exist != null) {
            exist.setWatchTime(LocalDateTime.now());
            watchHistoryMapper.updateById(exist);
        }
    }

    @Override
    public List<VideoCardDTO> getWatchHistory(Long userId) {
        List<WatchHistory> histories = watchHistoryMapper.selectList(
                new QueryWrapper<WatchHistory>()
                        .eq("user_id", userId)
                        .orderByDesc("watch_time")
        );

        if (histories.isEmpty()) return Collections.emptyList();

        List<Long> videoIds = histories.stream()
                .map(WatchHistory::getVideoId)
                .collect(Collectors.toList());

        List<VideoInfo> videos = videoInfoMapper.selectBatchIds(videoIds);
        return videoCardConvertUtil.convertToCardDTOList(videos);
    }

    @Override
    public boolean deleteWatchHistory(Long userId, Long videoId) {
        int count = watchHistoryMapper.delete(new QueryWrapper<WatchHistory>()
                .eq("user_id", userId)
                .eq("video_id", videoId));
        return count > 0;
    }

    @Override
    public void clearAllHistory(Long userId) {
        watchHistoryMapper.delete(new QueryWrapper<WatchHistory>()
                .eq("user_id", userId));
    }
}
