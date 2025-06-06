package com.backend.bilibili.service.admin.view.anime;

import com.backend.bilibili.pojo.anime.AnimeCarousel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface AnimeCarouseManageService {
    List<AnimeCarousel> getAllCarousel();
    AnimeCarousel getCarouselById(Long id);
    Map<String, Object> addCarouselWithImage(MultipartFile imageFile,String fileName);
    Map<String, Object> updateCarouselWithImage(Long id, MultipartFile imageFile);
    boolean deleteCarouselById(Long id) throws Exception;
}
