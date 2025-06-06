package com.backend.bilibili.controller.carousel;

import com.backend.bilibili.pojo.carousel.Carousel;
import com.backend.bilibili.service.carousel.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarouselController {
    @Autowired
    CarouselService carouselService;

    @GetMapping("/getAllCarousel")
    public List<Carousel> getAllCarousel() {
        return carouselService.getAllCarousels();
    }
    @GetMapping("/getCarouselByType")
    public List<Carousel> getCarouselByType(@RequestParam("type") String type) {
        return carouselService.getCarouselsByType(type);
    }
}
