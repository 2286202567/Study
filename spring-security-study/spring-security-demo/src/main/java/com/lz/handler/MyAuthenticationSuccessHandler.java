package com.lz.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        Map map = new HashMap<>();
        map.put("success",true);
        map.put("message","认证成功");
        map.put("data",authentication);
        response.getWriter().print(JSON.toJSONString(map));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
