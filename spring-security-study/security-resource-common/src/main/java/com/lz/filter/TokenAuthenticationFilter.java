package com.lz.filter;

import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//过滤器从请求头中获取到用户授权信息,封装成 UsernamePasswordAuthenticationToken 并设置到 securityContext中
//security在授权的时候会从 UsernamePasswordAuthenticationToken获取认证信息和授权信息进行授权
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //1.获取请求头中的明文token
        String tokenJson = request.getHeader("token");
        if(StringUtils.hasLength(tokenJson)){


            String authTokenJson = new String(Base64Utils.decodeFromString(tokenJson));

            Map<String,Object> map = JSON.parseObject(authTokenJson,Map.class);
            //2.获取到用户主体信息，权限列表
            String username = map.get("principal").toString();

            //权限列表
            List<String> authoritiesStr = (List<String>)map.get("authorities");

            //转换权限列表
            List<SimpleGrantedAuthority> authorities = new ArrayList<>(authoritiesStr.size());

            authoritiesStr.forEach( authStr ->{
                
                authorities.add(new SimpleGrantedAuthority(authStr));
            });

            //3.把用户主体信息，权限列表，交给Security
            //把用户信息和权限封装成 UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    username,null,authorities );

            //设置detail,根据请求对象创建一个detail
            token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            //把UsernamePasswordAuthenticationToken填充到security上下文
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        //放行
        filterChain.doFilter(request,response);
    }
}

