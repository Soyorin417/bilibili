package com.backend.bilibili.service.search.impl;

import com.backend.bilibili.mapper.search.UserSearchHistoryMapper;
import com.backend.bilibili.pojo.search.UserSearchHistory;
import com.backend.bilibili.service.search.UserSearchHistoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserSearchHistoryServiceImpl implements UserSearchHistoryService {

    @Autowired
    private UserSearchHistoryMapper searchHistoryMapper;

    @Override
    public void addSearchHistory(Long userId, String keyword) {
        QueryWrapper<UserSearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("keyword", keyword);

        UserSearchHistory existing = searchHistoryMapper.selectOne(wrapper);
        if (existing != null) {
            existing.setSearchTime(LocalDateTime.now());
            searchHistoryMapper.updateById(existing);
        } else {
            UserSearchHistory history = new UserSearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            history.setSearchTime(LocalDateTime.now());
            searchHistoryMapper.insert(history);
        }
    }

    @Override
    public List<UserSearchHistory> getSearchHistory(Long userId) {
        QueryWrapper<UserSearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("search_time");
        return searchHistoryMapper.selectList(wrapper);
    }

    @Override
    public void deleteSearchHistory(Long userId, Long id) {
        QueryWrapper<UserSearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("id", id);
        searchHistoryMapper.delete(wrapper);
    }

    @Override
    public void clearSearchHistory(Long userId) {
        QueryWrapper<UserSearchHistory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        searchHistoryMapper.delete(wrapper);
    }
}
