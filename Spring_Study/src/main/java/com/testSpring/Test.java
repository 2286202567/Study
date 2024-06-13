package com.testSpring;

import com.spring.TestApplicationContext;
import com.testSpring.service.UserService;
import com.testSpring.service.UserServiceImpl;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException {
        TestApplicationContext applicationContext = new TestApplicationContext(AppConfig.class);
        //UserServiceImpl userServiceImpl = (UserServiceImpl) applicationContext.getBean("userServiceImpl");
//        UserServiceImpl userService2 = (UserServiceImpl) applicationContext.getBean("userServiceImpl");
//        UserServiceImpl userService3 = (UserServiceImpl) applicationContext.getBean("userServiceImpl");
//        System.out.println(userServiceImpl);
//        System.out.pritln(userService2);
//        System.out.println(userService3);
        UserService userService = (UserService) applicationContext.getBean("userService");
        userService.test();
    }
}
