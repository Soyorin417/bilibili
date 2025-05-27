package com.backend.bilibili.controller.navbar;

import com.backend.bilibili.service.dto.NavBarActivityDTO;
import com.backend.bilibili.service.navbar.NavBarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/activity")
public class NavBarActivityController {

    @Autowired
    private NavBarService NavBarService;

    @GetMapping("/right")
    public List<NavBarActivityDTO> getRightActivityList() {
        return NavBarService.getRightActivities();
    }
}
