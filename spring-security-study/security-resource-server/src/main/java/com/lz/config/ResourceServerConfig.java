package com.lz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//资源服务配置
@Configuration
@EnableResourceServer   //开启资源服务配置
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    //配置资源id ,对应oauth_client_details表中配置的resourceIds
    //一个资源ID可以对应一个资源服务器，如果Token中不包含该资源ID就无法访问该资源服务器
    public static final String RESOURCE_ID = "res1";

    @Bean
    public TokenStore tokenStore(){
        //return new InMemoryTokenStore();
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    //JWT令牌校验工具
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //设置JWT签名密钥。它可以是简单的MAC密钥，也可以是RSA密钥
        jwtAccessTokenConverter.setSigningKey("123");
        return jwtAccessTokenConverter;
    }
    //资源服务器安全性配置
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //资源ID，请求中的Token必须有用该资源ID的访问权限才可以访问该资源服务器
        resources.resourceId(RESOURCE_ID);
        //指定TokenStore，使用JWT校验
        resources.tokenStore(tokenStore());
        //无状态
        resources.stateless(true);
        //验证令牌的服务，令牌验证通过才允许获取资源，使用远程校验
        //resources.tokenServices(resourceServerTokenServices());
    }

    //资源服务令牌验证服务,通过远程校验令牌
    /**
     @Bean
     public ResourceServerTokenServices resourceServerTokenServices(){
     //使用远程服务请求授权服务器校验token ， 即：资源服务和授权服务器不在一个主机
     RemoteTokenServices services = new RemoteTokenServices();
     //授权服务地址 , 当浏览器访问某个资源时就会调用该远程授权服务地址去校验token
     //要求请求中必须携带token
     services.setCheckTokenEndpointUrl("http://localhost:3000/oauth/check_token");
     //客户端id，对应认证服务的客户端详情配置oauth_client_details表中的clientId
     services.setClientId("webapp");
     //密钥，对应认证服务的客户端详情配置的秘钥
     services.setClientSecret("secret");
     return services;
     }
     */
    //SpringSecurity的相关配置
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //校验scope必须为all ， 对应认证服务的客户端详情配置的clientId
                .antMatchers("/**").access("#oauth2.hasScope('all')")
                //关闭跨域伪造检查
                .and().csrf().disable()
                //把session设置为无状态，意思是使用了token，那么session不再做数据的记录
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}