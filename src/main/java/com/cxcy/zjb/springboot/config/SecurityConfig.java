package com.cxcy.zjb.springboot.config;

import com.cxcy.zjb.springboot.handler.MyAuthenticationFailHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置类
 *
 * Created by LINWENHAO on 2018/8/16.
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //启用安全方法设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String KEY = "waylau.com";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyAuthenticationFailHandler authenticationFailHandler;



    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();   // 使用 BCrypt 加密
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder); // 设置密码加密方式
        return authenticationProvider;
    }


    /**
     * 自定义配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/css/**","/editormd/**", "/bootstrap/**", "/js/**", "/font/**","/jq/**", "/img/**","/index").permitAll() // 都可以访问
                .antMatchers("/admins/**").hasRole("ADMIN") // 需要相应的角色才能访问
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .formLogin()   //基于 Form 表单登录验证
                .loginPage("/login").defaultSuccessUrl("/login-success")// 自定义登录后界面
                .failureUrl("/login-error")
//                .failureHandler(authenticationFailHandler)
                .and().rememberMe().key(KEY) // 启用 remember me
                .and().exceptionHandling().accessDeniedPage("/403")  // 处理异常，拒绝访问就重定向到 403 页面
                .and()
                .cors()//新加入
                .and()
                .csrf().disable();

    }

    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
}
