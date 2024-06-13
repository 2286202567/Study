package com.lz;

public class HelloServiceImpl2 implements HelloService{
    @Override
    public String saveHell(String name) {
        return "Hello2："+name;
    }
}
