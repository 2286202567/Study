package com.lz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class ResourceServerConfig{


    //配置资源id ,跟AuthorizationServerConfig.configure配置的resourceIds一样
    public static final String RESOURCE_ID = "res1";

    //JWT相关配置===============================================
    @Bean
    public TokenStore tokenStore(){


        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    //设置JWT签名密钥。它可以是简单的MAC密钥，也可以是RSA密钥
    private final String sign_key  = "123";

    //JWT令牌校验工具
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){


        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //设置JWT签名密钥。它可以是简单的MAC密钥，也可以是RSA密钥
        jwtAccessTokenConverter.setSigningKey(sign_key);
        return jwtAccessTokenConverter;
    }

    //zuul资源服务配置，针对认证服务器的配置
    @Configuration
    @EnableResourceServer
    public class AuthConfig extends ResourceServerConfigurerAdapter {



        @Autowired
        private TokenStore tokenStore;

        //资源服务器安全性配置
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {


            //资源ID
            resources.resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore)
                    //无状态
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {


            //针对于直接放行的资源
            http.authorizeRequests().antMatchers("/**").permitAll();

        }
    }

    //微服务 资源服务配置
    @Configuration
    @EnableResourceServer
    public class ResourceConfig extends ResourceServerConfigurerAdapter{



        @Autowired
        private TokenStore tokenStore;

        //资源服务器安全性配置
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {


            //资源ID
            resources.resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore)
                    //无状态
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {


            //如果是其他微服务资源需要 oauth2 认证
            http.authorizeRequests()
                    //校验scope必须为all , 针对于/resource/路径的请求需要oauth2验证有ROLE_API的权限才能访问
                    .antMatchers("/services/resource/**").access("#oauth2.hasScope('resource1')")
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .and().cors().and().csrf().disable();
        }
    }
    //微服务 资源服务配置
    @Configuration
    @EnableResourceServer
    public class Resource2Config extends ResourceServerConfigurerAdapter{



        @Autowired
        private TokenStore tokenStore;

        //资源服务器安全性配置
        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {


            //资源ID
            resources.resourceId(RESOURCE_ID)
                    .tokenStore(tokenStore)
                    //无状态
                    .stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {


            //如果是其他微服务资源需要 oauth2 认证
            http.authorizeRequests()
                    //校验scope必须为all , 针对于/resource/路径的请求需要oauth2验证有ROLE_API的权限才能访问
                    .antMatchers("/services/resource2/**").access("#oauth2.hasScope('resource2')")
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .and().cors().and().csrf().disable();
        }
    }
    //如果有其他的资源服务还需要配置其他的 ResourceConfig
}

