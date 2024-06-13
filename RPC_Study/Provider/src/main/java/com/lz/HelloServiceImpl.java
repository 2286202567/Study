package com.lz;

public class HelloServiceImpl implements HelloService{
    @Override
    public String saveHell(String name) {
        return "Hello："+name;
    }
}
