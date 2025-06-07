package com.backend.bilibili.service.hotSearch.impl;

import com.backend.bilibili.mapper.hotSearch.HotSearchMapper;
import com.backend.bilibili.pojo.hotSearch.HotSearch;
import com.backend.bilibili.service.hotSearch.HotSearchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotSearchServiceImpl extends ServiceImpl<HotSearchMapper, HotSearch> implements HotSearchService {

    @Override
    public void incrementSearchCount(String keyword) {
        // 先查是否存在
        HotSearch existing = lambdaQuery()
                .eq(HotSearch::getKeyword, keyword)
                .one();

        if (existing == null) {
            HotSearch newRecord = new HotSearch();
            newRecord.setKeyword(keyword);
            newRecord.setSearchCount(1);
            newRecord.setLastSearchedAt(LocalDateTime.now());
            save(newRecord);
        } else {
            // 存在则更新计数和时间
            existing.setSearchCount(existing.getSearchCount() + 1);
            existing.setLastSearchedAt(LocalDateTime.now());
            updateById(existing);
        }
    }

    @Override
    public List<String> getTopHotSearches(int limit) {
        return lambdaQuery()
                .orderByDesc(HotSearch::getSearchCount)
                .last("LIMIT " + limit)
                .list()
                .stream()
                .map(HotSearch::getKeyword)
                .collect(Collectors.toList());
    }
}

