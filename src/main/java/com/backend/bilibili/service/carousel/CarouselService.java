package com.backend.bilibili.service.carousel;

import com.backend.bilibili.pojo.carousel.Carousel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CarouselService {
    Map<String, Object> addCarousel(MultipartFile imageFile, String type);
    boolean deleteCarouselById(Integer id);
    List<Carousel> getAllCarousels();
    Map<String, Object> updateCarousel(Integer id, MultipartFile imageFile, String type);
    List<Carousel> getCarouselsByType(String type);
}
