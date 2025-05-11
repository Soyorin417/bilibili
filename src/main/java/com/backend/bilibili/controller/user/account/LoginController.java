package com.backend.bilibili.controller.user.account;

import com.backend.bilibili.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/login")
    public Map<String, String> getToken(@RequestBody Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");

        return loginService.getToken(username, password);
    }
}
