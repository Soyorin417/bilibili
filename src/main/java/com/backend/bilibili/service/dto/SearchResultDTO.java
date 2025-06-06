package com.backend.bilibili.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResultDTO {
    private List<VideoCardDTO> videos;
    private List<UserDTO> users;
}
