package com.backend.bilibili.controller.navbar;

import com.backend.bilibili.service.dto.NavBarDTO;
import com.backend.bilibili.service.navbar.NavBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class NavBarActivityController {

    @Autowired
    private NavBarService NavBarService;

    @GetMapping("/activity")
    public List<NavBarDTO> getRightActivityList() {
        return NavBarService.getRightActivities();
    }

    @GetMapping("/collect")
    public List<NavBarDTO> getCollectActivityList() {
        return NavBarService.getCollection();
    }

    @GetMapping("/history")
    public List<NavBarDTO> getHistoryActivityList() {
        return NavBarService.getHistory();
    }
}
