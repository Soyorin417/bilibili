package com.backend.bilibili.controller.anime;

import com.backend.bilibili.pojo.anime.AnimeCarousel;
import com.backend.bilibili.service.anime.AnimeCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Controller
public class AnimeCarouselController {
    @Autowired
    AnimeCarouselService animeCarouselService;

    @GetMapping("/getAllCarousel")
    List<AnimeCarousel> getAllCarousel() {
        return animeCarouselService.getAllCarousel();
    }
}
