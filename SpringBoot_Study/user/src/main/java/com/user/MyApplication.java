package com.user;

import org.springboot.TestSpringApplication;
import org.springboot.TestSpringBootApplication;
import org.springframework.context.annotation.Import;

@TestSpringBootApplication
public class MyApplication {
    //    @Bean
//    public TomcatWebServer tomcatWebServer() {
//        return new TomcatWebServer();
//    }
//    @Bean
//    public JettyWebServer jettyWebServer() {
//        return new JettyWebServer();
//    }
    public static void main(String[] args) {
        TestSpringApplication.run(MyApplication.class);
    }

}
