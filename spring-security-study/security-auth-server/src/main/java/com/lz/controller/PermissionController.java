package com.lz.controller;

import com.lz.entity.Permission;
import com.lz.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/Permission")
public class PermissionController {
    @Resource
    private PermissionService permissionService;

    @GetMapping("/selectPermissionsByUserId/{userId}")
    public List<Permission> selectPermissionsByUserId(@PathVariable("userId") Integer userId) {
        return permissionService.selectPermissionsByUserId(userId);
    }

    @GetMapping("/selectAll")
    public List<Permission> selectAll() {
        return permissionService.selectAll();
    }
}
