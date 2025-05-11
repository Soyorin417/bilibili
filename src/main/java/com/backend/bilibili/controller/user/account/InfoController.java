package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoController {
    @Autowired
    private InfoService infoService;
    @GetMapping("/user")
    public Map<String, Object> userinfo() {
        return infoService.getinfo(); // 返回 Object 是正确的
    }

}
