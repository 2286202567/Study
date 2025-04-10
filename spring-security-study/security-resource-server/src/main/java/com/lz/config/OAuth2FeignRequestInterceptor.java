package com.lz.config;

import com.lz.util.HttpUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

@Component
@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {



    private static String TEMPTOKENURL = "http://localhost:3000/oauth/token?client_id=%s&client_secret=%s&grant_type=client_credentials";

    //请求头中的token
    private final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate requestTemplate) {


        //requestTemplate:feign底层用来发请求的http客户端
        //1.使用客户端模式生成一个临时的Token
        Map<String, String> tokenMap = HttpUtil.sendPost(String.format(TEMPTOKENURL, "temp", "123"));
        Assert.isTrue(tokenMap!=null,"服务调用失败[临时Token获取失败]");

        log.info("Feign拦截器添加临时Token到请求头：{}",tokenMap);

        //2.添加到Feign的请求头
        requestTemplate.header(AUTHORIZATION_HEADER,"Bearer "+tokenMap.get("access_token"));
    }
}
