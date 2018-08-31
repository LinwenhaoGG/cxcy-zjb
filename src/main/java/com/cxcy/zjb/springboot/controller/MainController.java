package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页控制层
 * <p>
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class MainController {
    /**
     * 获取登录界面
     *
     * @return
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        model.addAttribute("errorMsg", "登陆失败，账号或者密码错误！");
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/loginsuccess")
    public String define(Model model) {
        //判断是否已经登录
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (loginUser.getState().equals(1)) { //如果已经认证，跳转主页
                return "index";
            } else if (loginUser.getStyle().equals(1)) {  //如果为学生，则跳转学生认证页面
                return "student";
            } else if (loginUser.getStyle().equals(2)) {  //如果为教师，则跳转教师认证页面
                return "teacher";
            } else if (loginUser.getStyle().equals(3)) {  //如果为企业，则跳转企业认证页面
                return "company";
            }
        }
        //未登录则跳转登录页面
        return "login";
    }
    @GetMapping("/")
    public String getMain(){
        return "index";
    }
}
