package com.lz.service;

import com.lz.entity.Role;
import org.springframework.stereotype.Service;


public interface RoleService {
    Role selectRole(String name);
}
