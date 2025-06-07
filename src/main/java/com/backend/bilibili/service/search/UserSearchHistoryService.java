package com.backend.bilibili.service.search;

import com.backend.bilibili.pojo.search.UserSearchHistory;

import java.util.List;

public interface UserSearchHistoryService {
    void addSearchHistory(Long userId, String keyword);
    List<UserSearchHistory> getSearchHistory(Long userId);
    void deleteSearchHistory(Long userId, Long id);
    void clearSearchHistory(Long userId);
}