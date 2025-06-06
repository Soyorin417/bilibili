package com.backend.bilibili.service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class UploadVideoDTO {
    private String title;
    private MultipartFile videoFile;
    private MultipartFile coverFile;
    private String description;
    private String duration;
    private Long authorId;
    private MultipartFile danmakuFileName;
    private List<String> tags;
}
