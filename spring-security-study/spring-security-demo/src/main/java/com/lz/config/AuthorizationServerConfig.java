package com.lz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;


//授权服务器配置
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    //密碼編碼器
    @Autowired
    private PasswordEncoder passwordEncoder;

    //第一步：客户端详情配置============================
    //客户端详细信息服务配置：客戶端id,客戶端秘钥，授权方式等
    @Autowired
    private DataSource dataSource;

    //第二步：令牌服务配置=============================================

    //客户端详情service
    @Autowired
    private ClientDetailsService clientDetailsService;

    //认证管理器,在WebSecurityConfig中配置
    @Resource
    private AuthenticationManager authenticationManager;

    //定义针对于JDBC的客户端配置详情服务
    @Bean
    public ClientDetailsService clientDetailsService() {
        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
        //设置密码编码
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    //令牌存储 ， 基于JWT
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
        //jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    //配置令牌服务
    @Bean
    public AuthorizationServerTokenServices tokenService(){
        //创建默认的令牌服务
        DefaultTokenServices services = new DefaultTokenServices();
        //指定客户端详情配置
        services.setClientDetailsService(clientDetailsService());
        //支持产生刷新token
        services.setSupportRefreshToken(true);
        //token存储方式
        services.setTokenStore(tokenStore());

        //设置token增强 - 设置token转换器
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter()));

        services.setTokenEnhancer(tokenEnhancerChain);  //jwtAccessTokenConverter()
        //token有效时间
        services.setAccessTokenValiditySeconds(72000);
        //刷新令牌默认有效时间
        services.setRefreshTokenValiditySeconds(72000);
        return services;
    }

    //授权码服务
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        //基于内存存储的的授权码服务
        //return new InMemoryAuthorizationCodeServices();
        //基于内存存储的的授权码服务
        return new JdbcAuthorizationCodeServices(dataSource);
    }


    //基于jdbc的客户端详情配置方案
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //配置成基于jdbc的客户端详情方案
        clients.withClientDetails(clientDetailsService());
    }

    //配置令牌访问端点
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //密码授权模式需要
                .authenticationManager(authenticationManager)
                //授权码模式服务
                .authorizationCodeServices(authorizationCodeServices())
                //配置令牌管理服务
                .tokenServices(tokenService())
                //允许post方式请求
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    //第三步：端点安全约束======================================
    //配置令牌安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                //对应/oauth/token_key 公开，获取公钥需要访问该端点
                .tokenKeyAccess("permitAll()")
                //对应/oauth/check_token ，路径公开，校验Token需要请求该端点
                .checkTokenAccess("permitAll()")
                //允许客户端进行表单身份验证,使用表单认证申请令牌
                .allowFormAuthenticationForClients();
    }
}

