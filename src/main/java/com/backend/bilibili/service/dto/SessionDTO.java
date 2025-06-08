package com.backend.bilibili.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO {
    private Long sessionId;
    private String name;
    private String avatar;
    private String summary;
    private Integer unreadCount;
}