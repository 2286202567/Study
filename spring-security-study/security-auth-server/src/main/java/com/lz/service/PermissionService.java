package com.lz.service;

import com.lz.entity.Permission;
import org.springframework.stereotype.Service;

import java.util.List;


public interface PermissionService {
    List<Permission> selectPermissionsByUserId(Integer userId);
    List<Permission> selectAll();
}
