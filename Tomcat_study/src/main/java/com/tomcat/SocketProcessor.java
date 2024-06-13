package com.tomcat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class SocketProcessor implements Runnable {

    private Socket socket;
    private Tomcat tomcat;

    public Tomcat getTomcat() {
        return tomcat;
    }

    public SocketProcessor(Socket socket, Tomcat tomcat) {
        this.socket = socket;
        this.tomcat = tomcat;
    }

    @Override
    public void run() {
        processSocket(socket);
    }

    private void processSocket(Socket socket) {
        //处理Socket连接
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            inputStream.read(bytes);
            int pos = 0;
            //解析字节流  遇到一个空格就退出
            int begin = 0, end = 0;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == ' ') break;
            }
            //组合空格之前的字节流，转换成字符串就是请求方法
            StringBuilder stringBuilder = new StringBuilder();
            for (; begin < end; begin++) {
                stringBuilder.append((char) bytes[begin]);
            }
            System.out.println(stringBuilder.toString());//请求类型

            //解析URL
            pos++;
            begin++;
            end++;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == ' ') break;
            }
            //组合空格之前的字节流，转换成字符串就是请求方法
            StringBuilder url = new StringBuilder();
            for (; begin < end; begin++) {
                url.append((char) bytes[begin]);
            }
            System.out.println(url.toString());//请求类型


            //解析URL
            pos++;
            begin++;
            end++;
            for (; pos < bytes.length; pos++, end++) {
                if (bytes[pos] == '\r') break;
            }
            //组合空格之前的字节流，转换成字符串就是请求方法
            StringBuilder protocl = new StringBuilder();
            for (; begin < end; begin++) {
                protocl.append((char) bytes[begin]);
            }
            System.out.println(protocl.toString());//请求类型

            Request request = new Request(stringBuilder.toString(), url.toString(), protocl.toString(), socket);
            Response response = new Response(request);
            //匹配Servlet、doGet
//            TomcatServlet tomcatServlet = new TomcatServlet();
//            tomcatServlet.service(request,response);

            String requestUrl = request.getRequestURL().toString();
            requestUrl = requestUrl.substring(1);
            String[] parts = requestUrl.split("/");
            String appName = parts[0];
            Context context = tomcat.getContextMap().get(appName);

            if (parts.length > 1) {
                Servlet servlet = context.getByUrlPattern(parts[1]);
                if (servlet != null) {
                    servlet.service(request, response);
                } else {
                    DefaultServlet defaultServlet = new DefaultServlet();
                    defaultServlet.service(request, response);
                }
                response.complate();
            }

        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }
}
