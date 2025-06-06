package com.backend.bilibili.controller.video;

import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.UploadVideoDTO;
import com.backend.bilibili.service.dto.VideoViewDTO;
import com.backend.bilibili.service.video.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoInfoService videoInfoService;

    @GetMapping("/getAllVideo")
    public List<VideoViewDTO> getAllVideoViews() {
        return videoInfoService.getAllVideoViews();
    }

    @GetMapping("/getVideoById")
    public VideoViewDTO getVideoById(@RequestParam int id) {
        return videoInfoService.getVideoInfoById(id);
    }

    @GetMapping("/user/{userId}")
    public List<VideoViewDTO> getVideosByUserId(@PathVariable int userId) {
        return videoInfoService.getAllVideoViewsByUserId(userId);
    }

    @PostMapping("/play/{videoId}")
    public ResponseEntity<String> addPlayCount(@PathVariable Long videoId) {
        return videoInfoService.increaseViewCount(videoId)
                ? ResponseEntity.ok("success")
                : ResponseEntity.status(500).body("fail");
    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitVideo(@RequestParam String title,
                                         @RequestParam String videoFileName,
                                         @RequestParam String coverFileName,
                                         @RequestParam String description,
                                         @RequestParam String duration,
                                         @RequestParam Long authorId,
                                         @RequestParam String danmakuFileName
    ) {
        return videoInfoService.submitVideoWithParams(title, videoFileName, coverFileName, description, duration, authorId,danmakuFileName);
    }

    @PostMapping("/submit-test")
    public ResponseEntity<?> submitVideoTest(@ModelAttribute UploadVideoDTO dto) {
        System.out.println(dto);
        return videoInfoService.submitVideoTest(dto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        return videoInfoService.deleteVideoById(id)
                ? ResponseEntity.ok("删除成功")
                : ResponseEntity.status(404).body("视频不存在");
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody VideoInfo videoInfo) {
        return videoInfoService.updateVideo(videoInfo)
                ? ResponseEntity.ok("更新成功")
                : ResponseEntity.status(404).body("更新失败");
    }

    @GetMapping("/{id}")
    public VideoViewDTO getById(@PathVariable int id) {
        return videoInfoService.getVideoInfoById(id);
    }
}


