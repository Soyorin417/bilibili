package com.backend.bilibili.service.anime;


import com.backend.bilibili.pojo.anime.Anime;

import java.util.List;

public interface AnimeService {
    public Anime getAnime(int id);
    public List<Anime> getAllAnimes();
}
