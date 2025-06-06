package com.backend.bilibili.service.anime;

import com.backend.bilibili.pojo.anime.AnimeCarousel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AnimeCarouselService {
    List<AnimeCarousel> getAllCarousel();
}
