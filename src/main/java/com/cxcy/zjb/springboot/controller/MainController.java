package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 主页控制层
 * <p>
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class MainController {
    @Autowired
    private UserService userService;

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

    @GetMapping("/test")
    public String test() {
        return "top";
    }

    @GetMapping("/login-success")
    public ModelAndView define(Model model) {
        //判断是否已经登录
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User UserInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User loginUser = userService.findUserById(UserInfo.getId());
            if (loginUser.getState().equals(1)) { //如果已经认证，跳转主页
                return new ModelAndView("index","model",model);
            } else if (loginUser.getState().equals(0)){
                Integer uStyle = loginUser.getStyle();
                model.addAttribute("uStyle", uStyle);
                return new ModelAndView("validate","model",model);
            } else if (loginUser.getState().equals(2)){
                return new ModelAndView("waitTip","model",model);
            }else  if (loginUser.getState().equals(3)){
                return new ModelAndView("failTip","model",model);
            }
        }
        //未登录则跳转登录页面
        return new ModelAndView("login","model",model);
    }
    @GetMapping("/")
    public String getMain(){
        return "index";
    }
}
