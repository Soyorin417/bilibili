package com.backend.bilibili.service.anime.impl;

import com.backend.bilibili.mapper.anime.AnimeCarouselMapper;
import com.backend.bilibili.pojo.anime.AnimeCarousel;
import com.backend.bilibili.service.anime.AnimeCarouselService;
import com.backend.bilibili.service.minio.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnimeCarouselServiceImpl implements AnimeCarouselService {

    @Autowired
    private AnimeCarouselMapper animeCarouselMapper;

    @Autowired
    private MinioService minioService;

    //获取全部轮播图
    @Override
    public List<AnimeCarousel> getAllCarousel() {
        return animeCarouselMapper.selectList(null);
    }
}
