package com.backend.bilibili.controller.anime;

import com.backend.bilibili.pojo.anime.AnimeSchedule;
import com.backend.bilibili.service.anime.AnimeScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Controller
public class AnimeScheduleController {
    @Autowired
    AnimeScheduleService animeScheduleService;

    @GetMapping("/getAllAnimeSchedule")
    public List<AnimeSchedule> getAllAnimeSchedule() {
        return animeScheduleService.getAllAnimeSchedule();
    }
}
