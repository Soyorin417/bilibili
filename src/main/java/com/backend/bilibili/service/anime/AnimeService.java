package com.backend.bilibili.service.anime;


import com.backend.bilibili.pojo.anime.Anime;

import java.util.List;

public interface AnimeService {
    Anime getAnime(int id);
    List<Anime> getAllAnimes();
}
