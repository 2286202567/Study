package com.lz.service.impl;

import com.lz.entity.Role;
import com.lz.mapper.RoleMapper;
import com.lz.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleMapper roleMapper;
    @Override
    public Role selectRole(String name) {
        Role role= roleMapper.selectRole(name);
        if(role == null){
            return new Role(1,"ll","123");
        }
        return role;
    }
}
