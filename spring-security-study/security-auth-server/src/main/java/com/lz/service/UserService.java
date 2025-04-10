package com.lz.service;

import com.lz.entity.User;
import org.springframework.stereotype.Service;

public interface UserService {
    User selectByUsername(String userName);
}
