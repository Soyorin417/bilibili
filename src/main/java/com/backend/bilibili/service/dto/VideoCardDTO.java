package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCardDTO {
    private Long id;

    private String title;

    private Long views;

    private int comments;

    private Date time;

    private String description;

    private String videoUrl;

    private String image;

    private String duration;

    private String author;
}
