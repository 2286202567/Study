package com.lz.mapper;

import com.lz.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    Role selectRole(String name);
}
