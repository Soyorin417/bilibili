package com.backend.bilibili.pojo.video;

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

    private String views;

    private String comments;

    private Date time;

    private String description;

    private String videoUrl;

    private String image;

    private String duration;

    private String author;
}
