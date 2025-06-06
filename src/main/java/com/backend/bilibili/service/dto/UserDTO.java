package com.backend.bilibili.service.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String avatar;
    private int level;
    private boolean isFollow;
}
