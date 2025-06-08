package com.backend.bilibili.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DanmuDTO {

    @JsonProperty("time_in_video")
    private double time;

    @JsonProperty("mode")
    private int type;

    @JsonProperty("font_size")
    private int fontSize;

    @JsonProperty("font_color")
    private String color;

    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("pool")
    private int pool;

    @JsonProperty("user_hash")
    private String userHash;

    @JsonProperty("dmid")
    private String rowID;

    @JsonProperty("track")
    private int track = 0;

    @JsonProperty("text")
    private String text;

    @JsonProperty("is_advanced")
    private boolean isAdvanced;

    @JsonProperty("duration")
    private double duration = 8;

    @JsonProperty("font")
    private String font = "SimHei";

    @JsonProperty("is_scrolling")
    private boolean isScrolling;

    @JsonProperty("position")
    private Map<String, Object> position;

}
