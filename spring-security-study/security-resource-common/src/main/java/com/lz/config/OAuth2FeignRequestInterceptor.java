package com.lz.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    //请求头中的token
    private final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {


        //requestTemplate:feign底层用来发封装请求的对象

        //1.获取请求对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        //2.获取请求头中的Token
        String token = requestAttributes.getRequest().getHeader(AUTHORIZATION_HEADER);
        log.info("Feign拦截器添加请求头：{}",token);

        //3.添加到Feign的请求头
        requestTemplate.header(AUTHORIZATION_HEADER,token);
    }
}

