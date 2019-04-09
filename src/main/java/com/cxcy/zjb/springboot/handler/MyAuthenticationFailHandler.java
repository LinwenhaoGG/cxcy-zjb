package com.cxcy.zjb.springboot.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyAuthenticationFailHandler implements AuthenticationFailureHandler {

    public static final String RETURN_TYPE = "html"; // 登录失败时，用来判断是返回json数据还是跳转html页面

    @Autowired
    private ObjectMapper objectMapper;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, org.springframework.security.core.AuthenticationException e) throws IOException, ServletException {
        log.info("登录失败:" + e.getMessage());
        log.info("username=>" + httpServletRequest.getParameter("username"));
        if (e.getCause().getMessage().equals("noUser")) {
            httpServletRequest.setAttribute("msg", "该账号已经被停用，请联系管理员");
        } else {
            httpServletRequest.getSession().setAttribute("msg", "登陆失败，账号或者密码错误");
        }
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/login-error");
    }

}