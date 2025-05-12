package com.backend.bilibili.controller.anime;

import com.backend.bilibili.pojo.anime.Anime;
import com.backend.bilibili.service.anime.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Controller
public class AnimeController {
    @Autowired
    AnimeService animeService;

    @GetMapping("/getAllAnime")
    public List<Anime> getAllAnime() {
        return animeService.getAllAnimes();
    }
}
