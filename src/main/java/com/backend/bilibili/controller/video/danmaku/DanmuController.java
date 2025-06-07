package com.backend.bilibili.controller.video.danmaku;

import com.backend.bilibili.pojo.video.danmaku.Danmu;
import com.backend.bilibili.service.video.danmaku.DanmuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/danmu")
public class DanmuController {

    @Autowired
    private DanmuService danmuService;

    @PostMapping("/add")
    public String addDanmu(@RequestBody Danmu danmu) {
        danmuService.addDanmu(danmu);
        return "发送成功";
    }

    @GetMapping("/list/{videoId}")
    public List<Danmu> getDanmuList(@PathVariable Long videoId) {
        return danmuService.getDanmuByVideoId(videoId);
    }

}
