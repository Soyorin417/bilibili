package com.backend.bilibili.service.anime.impl;

import com.backend.bilibili.mapper.anime.AnimeMapper;
import com.backend.bilibili.pojo.anime.Anime;
import com.backend.bilibili.service.anime.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AnimeServiceImpl implements AnimeService {
    @Autowired
    AnimeMapper animeMapper;

    @Override
    public Anime getAnime(int id) {
        return null;
    }

    @Override
    public List<Anime> getAllAnimes() {
        return animeMapper.selectList(null);
    }
}
