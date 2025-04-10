package com.lz.service;

import org.springframework.stereotype.Service;

@Service("fun")
public class FunService {
    public static String getString(String meg){
        return "123456";
    }

    public static boolean getBoolean(String meg){
        return true;
    }
}
