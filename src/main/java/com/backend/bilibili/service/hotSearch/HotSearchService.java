package com.backend.bilibili.service.hotSearch;

import java.util.List;

public interface HotSearchService {

    void incrementSearchCount(String keyword);
    List<String> getTopHotSearches(int limit);
}
