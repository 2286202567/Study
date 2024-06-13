package com.user;


import com.mybatis.MapperProxyFactory;
import com.user.mapper.UserMapper;
import com.user.pojo.User;

import java.util.List;

public class MyApplication {
    public static void main(String[] args) {
        UserMapper userMapper = MapperProxyFactory.getMapper(UserMapper.class);
        List<User> userList = userMapper.getUser("张三",15);
        if(!userList.isEmpty()){
            userList.stream().distinct().forEach(user -> System.out.println(user));
        }

    }
}
