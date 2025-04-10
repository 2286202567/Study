package com.lz.controller;

import com.lz.entity.User;
import com.lz.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/User")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/selectByUsername/{userName}")
    public User selectByUsername(@PathVariable("userName") String userName) {
        return userService.selectByUsername(userName);
    }
}
