package com.backend.bilibili.controller.video.danmaku;

import com.backend.bilibili.pojo.video.danmaku.Danmaku;
import com.backend.bilibili.service.video.danmaku.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Controller
public class DanmakuController {
    @Autowired
    private DanmakuService danmakuService;
    @GetMapping("/danmaku/getDanmakuList")
    public Danmaku getDanmakuById(@RequestParam int id) {
        return danmakuService.getDanmaku(id);
    }

    @GetMapping("/danmaku/getBilibiliDanmaku")
    public ResponseEntity<String> getBilibiliDanmaku(@RequestParam String oid) throws IOException {
        oid = oid.trim();
        String xml = danmakuService.fetchDanmakuXml(oid);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/xml;charset=UTF-8")
                .body(xml);
    }

}
