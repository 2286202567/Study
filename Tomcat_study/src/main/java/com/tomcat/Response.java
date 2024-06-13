package com.tomcat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response extends AbstractHttpServeltResponse {
    private int status = 200;
    private String message = "OK";
    private static byte SP = ' ';
    private static byte CR = '\r';
    private static byte LF = '\n';

    private Map<String, String> headers = new HashMap<>();
    private Request request;
    private OutputStream socketOutputStream;
    private ResposeServletOutputStream resposeServletOutputStream = new ResposeServletOutputStream();

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public Response(Request request) {
        this.request = request;
        try {
            this.socketOutputStream = request.getSocket().getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public ResposeServletOutputStream getOutputStream() throws IOException {
        return resposeServletOutputStream;
    }


    public void complate() throws IOException {
        //发送相应
        //1.相应行
        sendResponseLine();
        //2.响应头
        sendResponseHeader();
        //3.响应体
        sendResponseBody();
    }

    private void sendResponseLine() throws IOException {
        socketOutputStream.write(request.getProtocol().getBytes());
        socketOutputStream.write(SP);
        socketOutputStream.write(status);
        socketOutputStream.write(SP);
        socketOutputStream.write(message.getBytes());
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);

    }

    private void sendResponseHeader() throws IOException {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            socketOutputStream.write(key.getBytes());
            socketOutputStream.write(":".getBytes());
            socketOutputStream.write(value.getBytes());
            socketOutputStream.write(CR);
            socketOutputStream.write(LF);
        }
        socketOutputStream.write(CR);
        socketOutputStream.write(LF);
    }

    private void sendResponseBody() throws IOException {
        socketOutputStream.write(getOutputStream().getBytes());
    }
}
