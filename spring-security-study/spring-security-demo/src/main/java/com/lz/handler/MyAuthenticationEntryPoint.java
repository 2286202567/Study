package com.lz.handler;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        e.printStackTrace();
        httpServletResponse.setContentType("application/json;charset=utf-8");
        Map<String,Object> result = new HashMap<>();
        result.put("success",false);
        result.put("message","登录失败，用户名或密码错误["+e.getMessage()+"]");
        httpServletResponse.getWriter().print(JSON.toJSONString(result));
    }
}
