package com.lz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//security 的配置
@Configuration
@EnableWebSecurity  //开启Security
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    //授权规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {



        http.csrf().disable()   //屏蔽跨域防护
                .authorizeRequests()          //对请求做授权处理
                .antMatchers("/**").permitAll(); //其他路径都要拦截
    }
}
