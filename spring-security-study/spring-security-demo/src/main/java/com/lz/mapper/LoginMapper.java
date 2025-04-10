package com.lz.mapper;

import com.lz.entity.Login;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    Login selectByUsername(String username);
}
