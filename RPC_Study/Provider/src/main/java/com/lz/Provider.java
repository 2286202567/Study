package com.lz;

import com.lz.common.URL;
import com.lz.protocol.HttpServer;
import com.lz.register.LocalRegister;
import com.lz.register.MapRemoteRegister;
import org.apache.juli.logging.LogFactory;

public class Provider {
    public static void main(String[] args) {
        //先进行本地注册
        new LocalRegister().register(HelloService.class.getName(),"1.0",HelloServiceImpl.class);
        //new LocalRegister().register(HelloService.class.getName(),"2.0",HelloServiceImpl2.class);

        //注册中心注册
        URL url = new URL("localhost", 8081);
        new MapRemoteRegister().register(HelloService.class.getName(),url);

        HttpServer httpServer = new HttpServer();
        httpServer.start(url.getHostName(),url.getPort());
    }
}
