package org.springboot;

import org.springframework.context.annotation.Conditional;

public class TomcatWebServer implements WebServer{

    @Override
    public TomcatWebServer start() {
        return new TomcatWebServer();
    }

    @Override
    public TomcatWebServer end() {
        return new TomcatWebServer();
    }
}
