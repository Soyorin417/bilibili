package com.backend.bilibili.controller.profile;


import com.backend.bilibili.service.dto.VideoCardDetailDTO;
import com.backend.bilibili.service.profile.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dynamic")
public class DynamicController {

    @Autowired
    private DynamicService dynamicService;

    @GetMapping("/list")
    public List<VideoCardDetailDTO> getUserDynamics() {
        return dynamicService.getUserDynamics();
    }
}
