package com.backend.bilibili.service.navbar;

import com.backend.bilibili.service.dto.NavBarActivityDTO;

import java.util.List;

public interface NavBarService {
    List<NavBarActivityDTO> getRightActivities();
}
