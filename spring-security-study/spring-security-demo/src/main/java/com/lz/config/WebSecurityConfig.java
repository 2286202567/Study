package com.lz.config;

import com.lz.entity.Permission;
import com.lz.handler.MyLogoutHandler;
import com.lz.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PermissionMapper permissionMapper;

    //提供用户信息，这里应该从数据库中查询
    @Bean
    public UserDetailsService userDetailService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("lz").password("123").authorities("admin").build());
        return manager;
    }

    //密码解码器，这里不解码
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

//    //授权规则配置
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()                //授权配置
//                .antMatchers("/login").permitAll()     //登录路径放行
//                .anyRequest().authenticated()      //其他路径都需要经过验证
//                .and().formLogin()                 //允许表单登录
//                .successForwardUrl("/loginSuccess")  //设置成功登录页
//                .and().logout().permitAll()         //登出路径放行
//                .and().csrf().disable();            //关闭跨域伪造检查
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable() //关闭CSRF跨站点请求伪造防护
                .authorizeRequests()    //对请求做授权处理
                .antMatchers("/login").permitAll()  //登录路径放行
                .antMatchers("/login.html").permitAll() //对登录页面跳转路径放行
                .anyRequest().authenticated() //其他路径都要拦截
                .and().formLogin()  //允许表单登录， 设置登陆页
                .successForwardUrl("/loginSuccess") // 设置登陆成功页
                .loginPage("/login.html")   //登录页面跳转地址
                .loginProcessingUrl("/login")   //登录处理地址(必须)
                .and().logout().logoutUrl("/mylogout").permitAll()    //制定义登出路径
                .logoutSuccessHandler((LogoutSuccessHandler) new MyLogoutHandler())  //登出后处理器-可以做一些额外的事情
                .invalidateHttpSession(true); //登出后session无效
                //.successForwardUrl("/loginSuccess") // 设置登陆成功页
//                .successHandler(new MyAuthenticationSuccessHandler)
//                .failureHandler(new MyAuthenticationFailureHandler)
//                .csrf().disable()   //关闭CSRF跨站点请求伪造防护
//                .authorizeRequests()          //对请求做授权处理
//                .antMatchers("/login").permitAll()  //登录路径放行
//                .antMatchers("/login.html").permitAll();//对登录页面跳转路径放行
//        http.formLogin()
                //.successForwardUrl("/loginSuccess") // 设置登陆成功页
//                .successHandler(new MyAuthenticationSuccessHandler)
//                .failureHandler(new MyAuthenticationFailureHandler);


//        //动态添加授权:从数据库动态查询出，哪些资源需要什么样的权限
//        for(Permission permission : permissions){
//            System.out.println(permission.getResource()+" - "+permission.getSn());
//            //如： /employee/list    需要     employee:list 权限才能访问
//            expressionInterceptUrlRegistry.antMatchers(permission.getResource()).hasAuthority(permission.getSn());
//        }
//
//        expressionInterceptUrlRegistry
//                .anyRequest().authenticated() //其他路径都要拦截
//                .and().formLogin()  //允许表单登录， 设置登陆页
//                .successForwardUrl("/loginSuccess") // 设置登陆成功页
//                .loginPage("/login.html")   //登录页面跳转地址
//                .loginProcessingUrl("/login")   //登录处理地址
//                .and().logout().permitAll();    //登出

    }

}
