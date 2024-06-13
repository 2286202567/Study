package com.tomcat;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

public class Context {
    private String appName;
    private Map<String, Servlet> urlPatternMapping = new HashMap<>();

    public Context(String appName) {
        this.appName = appName;
    }

    public void addUrlPatternMapping(String appName, Servlet servlet) {
        urlPatternMapping.put(appName, servlet);
    }


    public Servlet getByUrlPattern(String urlPattern) {
        for (String key : urlPatternMapping.keySet()) {
            if (key.contains(urlPattern)) {
                return urlPatternMapping.get(urlPattern);
            }
        }
        return null;
    }
}
