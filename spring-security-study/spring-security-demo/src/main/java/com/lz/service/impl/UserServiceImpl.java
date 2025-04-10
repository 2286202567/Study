package com.lz.service.impl;

import com.lz.entity.Login;
import com.lz.mapper.LoginMapper;
import com.lz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Login loginFromMysql = loginMapper.selectByUsername(username);
        if (loginFromMysql == null) {
            throw new UsernameNotFoundException("无效的用户名");
        }
        //暂时没加载权限
        List<GrantedAuthority> permissions = new ArrayList<>();
        //密码是基于BCryptPasswordEncoder加密的密文
        //User是security内部的对象，UserDetails的实现类 ，
        //用来封装用户的基本信息（用户名，密码，权限列表）
        //四个true分别是账户启用，账户过期，密码过期，账户锁定
        return new User(username, loginFromMysql.getPassword(), true, true, true,
                true, permissions);
    }

}
