package com.lz.controller;

import com.lz.entity.Role;
import com.lz.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @GetMapping("/selectRole/{name}")
    public Role selectRole(@PathVariable("name") String name) {
        return roleService.selectRole(name);
    }
}
