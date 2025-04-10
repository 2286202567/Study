package com.lz.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @PostMapping("/loginSuccess")
    public String loginSucess() {
        return "登陆成功qq";
    }
}
