package com.lz.service.impl;

import com.lz.entity.Permission;
import com.lz.entity.User;
import com.lz.mapper.PermissionMapper;
import com.lz.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 加载数据库中的认证的用户的信息：用户名，密码，用户的权限列表
     *
     * @param username: 该方法把username传入进来，
     *                  我们通过username查询用户的信息(密码，权限列表等)
     *                  然后封装成 UserDetails进行返回 ，交给security 。
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //User是security内部的对象，UserDetails的实现类 ，
        //用来封装用户的基本信息（用户名，密码，权限列表）
        //public User(String username, String password, Collection<? extends GrantedAuthority> authorities)
        User userFromDB = userMapper.selectByUsername(username);
        if (null == userFromDB) {
            throw new RuntimeException("无效的用户");
        }

        //模拟存储在数据库的用户的密码：123
        //String password = passwordEncoder.encode("123");
        String password = userFromDB.getPassWord();

        //查询用户的权限
        List<Permission> permission = permissionMapper.selectPermissionsByUserId(userFromDB.getId());

        //用户的权限列表,暂时为空
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        permission.forEach(e -> {
            System.out.println("用户：" + userFromDB.getUserName() + " 加载权限：" + e.getExpression());
            authorities.add(new SimpleGrantedAuthority(e.getExpression()));
        });
        //注意：这里的User是Security的User不是我们自己的User
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, password, authorities);
        return user;
    }
}
