package org.springboot;

import org.eclipse.jetty.util.Jetty;

public class JettyWebServer implements WebServer {
    @Override
    public JettyWebServer start() {
        return new JettyWebServer();
    }

    @Override
    public JettyWebServer end() {
        return new JettyWebServer();
    }
}
