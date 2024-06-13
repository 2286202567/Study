package com.tomcat;

import javax.servlet.Servlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Tomcat {

    private Map<String,Context> contextMap = new HashMap<>();

    public void start() {
        //建立Socket连接TCP
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            ExecutorService executorService = Executors.newFixedThreadPool(20);
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new SocketProcessor(socket,this));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        tomcat.deployApps();
        tomcat.start();
    }

    private void deployApps() {

        File webapps = new File(System.getProperty("user.dir"), "webapps");
        for (String app : webapps.list()) {
//            System.out.println(app);
            deployApp(webapps, app);
        }
    }

    private void deployApp(File webapps, String app) {
        //添加到缓存
        Context context = new Context(app);
        //目录下有哪些Servlet
        File appDirectory = new File(webapps, app);
        File classesDirectory = new File(appDirectory, "classes");
        List<File> allFilePath = getAllFilePath(classesDirectory);
        for (File clazz : allFilePath) {
            //loadClass 加载器加载类
            String name = clazz.getPath();
            name = name.replace(classesDirectory.getPath() + "\\", "");
            name = name.replace(".class", "");
            name = name.replace("\\", ".");
//            System.out.println(name);

            try {
                WebappClassLoader webappClassLoader = new WebappClassLoader(new URL[]{classesDirectory.toURL()});
                Class<?> servletClass = webappClassLoader.loadClass(name);
                if(HttpServlet.class.isAssignableFrom(servletClass)){
                    if(servletClass.isAnnotationPresent(WebServlet.class)){
                        WebServlet annotation = servletClass.getAnnotation(WebServlet.class);
                        String[] urlPatterns = annotation.urlPatterns();
                        for (String urlPattern : urlPatterns) {
                            context.addUrlPatternMapping(urlPattern, (Servlet) servletClass.newInstance());
                        }
                    }
                }

            } catch (MalformedURLException | ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            contextMap.put(app,context);
        }
    }

    //递归找到文件夹下所有的类
    public List<File> getAllFilePath(File srcFile) {
        ArrayList<File> result = new ArrayList<>();
        File[] files = srcFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    result.addAll(getAllFilePath(file));
                } else {
                    result.add(file);
                }
            }
        }
        return result;
    }

    public Map<String, Context> getContextMap() {
        return contextMap;
    }
}
