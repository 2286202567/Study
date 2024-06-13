package com.user.mapper;

import com.mybatis.Param;
import com.mybatis.Selector;
import com.user.pojo.User;

import java.util.List;

public interface UserMapper {
    @Selector("select * from User where name = #{name} and age =#{age}")
    public List<User> getUser(@Param("name") String name,@Param("age") int age);
    @Selector("select * from User where id = #{id}")
    public User getUserById(Integer id);
}
