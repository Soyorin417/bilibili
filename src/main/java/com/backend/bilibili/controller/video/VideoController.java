package com.backend.bilibili.controller.video;

import com.backend.bilibili.pojo.video.VideoInfo;
import com.backend.bilibili.service.dto.VideoViewDTO;
import com.backend.bilibili.service.minio.MinioService;
import com.backend.bilibili.service.video.VideoInfoService;
import com.backend.bilibili.utils.MinioUrlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@Controller
@RestController
public class VideoController {
    @Autowired
    private VideoInfoService videoInfoService;

    @Autowired
    private MinioService minioService;

    @GetMapping("/video/getAllVideo")
    public List<VideoViewDTO> getAllVideoViews() {
        return videoInfoService.getAllVideoViews();
    }

    @GetMapping("/video/getVideoById")
    public VideoViewDTO getVideoById(@RequestParam int id) {
        return videoInfoService.getVideoInfoById(id);
    }
    @PostMapping("/video/play/{videoId}")
    public ResponseEntity<String> addPlayCount(@PathVariable Long videoId) {
        boolean result = videoInfoService.increaseViewCount(videoId);
        return result ? ResponseEntity.ok("success") : ResponseEntity.status(500).body("fail");
    }


    @PostMapping("/submit")
    public ResponseEntity<String> submitVideo(
            @RequestParam String title,
            @RequestParam String videoFileName,
            @RequestParam String coverFileName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String author) {

        String cleanVideoFileName = extractFileName(videoFileName);
        String cleanCoverFileName = extractFileName(coverFileName);

        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setTitle(title);
        videoInfo.setVideoUrl(MinioUrlUtil.getUrl(cleanVideoFileName));
        videoInfo.setImage(MinioUrlUtil.getUrl(cleanCoverFileName));
        videoInfo.setDescription(description);
        videoInfo.setTime(new Date());

        System.out.println("准备调用service.submitVideo，videoInfo: " + videoInfo);

        boolean success = videoInfoService.submitVideo(videoInfo);
        System.out.println("submitVideo 返回: " + success);

        if (success) {
            return ResponseEntity.ok("提交成功");
        } else {
            return ResponseEntity.status(500).body("提交失败");
        }
    }

    private String extractFileName(String fullPath) {
        if (fullPath == null) {
            return "";
        }
        String hostPrefix = "113.45.69.13:9000";
        if (fullPath.startsWith(hostPrefix)) {
            return fullPath.substring(hostPrefix.length());
        }
        int lastSlashIndex = fullPath.lastIndexOf('/');
        if (lastSlashIndex >= 0 && lastSlashIndex < fullPath.length() - 1) {
            return fullPath.substring(lastSlashIndex + 1);
        }
        return fullPath;
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

