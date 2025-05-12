package com.backend.bilibili.service.anime.impl;

import com.backend.bilibili.mapper.anime.AnimeScheduleMapper;
import com.backend.bilibili.pojo.anime.AnimeSchedule;
import com.backend.bilibili.service.anime.AnimeScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnimeScheduleServiceImpl implements AnimeScheduleService {
    @Autowired
    AnimeScheduleMapper animeScheduleMapper;
    @Override
    public List<AnimeSchedule> getAllAnimeSchedule() {
        return animeScheduleMapper.selectList(null);
    }
}
