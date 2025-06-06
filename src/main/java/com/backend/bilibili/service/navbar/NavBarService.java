package com.backend.bilibili.service.navbar;

import com.backend.bilibili.service.dto.NavBarDTO;

import java.util.List;

public interface NavBarService {
    List<NavBarDTO> getRightActivities();
    List<NavBarDTO> getCollection();
    List<NavBarDTO> getHistory();
}
