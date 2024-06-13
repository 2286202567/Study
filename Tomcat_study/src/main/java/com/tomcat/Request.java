package com.tomcat;

import java.net.Socket;

public class Request extends AbstractHttpServeltRequest {
    private String method;
    private String url;
    private String protocol;
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public Request(String method, String url, String protocol, Socket socket) {
        this.method = method;
        this.url = url;
        this.protocol = protocol;
        this.socket = socket;
    }

    public String getMethod() {
        return method;
    }


    public StringBuffer getRequestURL() {
        return new StringBuffer(url);
    }

    public String getProtocol() {
        return protocol;
    }

}
