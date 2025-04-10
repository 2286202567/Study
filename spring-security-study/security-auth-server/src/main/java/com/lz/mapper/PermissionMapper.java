package com.lz.mapper;

import com.lz.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper {
    List<Permission> selectPermissionsByUserId(Integer userId);
    List<Permission> selectAll();
}
