package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.InformationService;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 主页控制层
 * <p>
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private InformationService informationService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private ProductionService productionService;
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
        model.addAttribute("errorMsg", "登陆失败，账号或者密码错误");
//        model.addAttribute("errorMsg", request.getSession().getAttribute("msg"));
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
    public ModelAndView define(Model model,HttpServletRequest request, HttpServletResponse response) {
        //判断是否已经登录
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User UserInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (UserInfo.getIsUse() == 0) {
                model.addAttribute("loginError", true);
                model.addAttribute("errorMsg", "该账号已经被停用，请联系管理员");
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null){
                    new SecurityContextLogoutHandler().logout(request, response, auth);
                }
                return new ModelAndView("login","model",model);
            }
            if (UserInfo.getState().equals(1)) { //如果已经认证，跳转主页
                List<Information> informationList = informationService.getInformationListTop(4); //获取前四个咨询
                List<Matchs> matchList = matchService.getMatchListTop(3); //获取前三个比赛
                List<Production> productionList = productionService.getProductionListTop(3); //拿到最新的3个作品
                model.addAttribute("informationList", informationList);
                model.addAttribute("matchList", matchList);
                model.addAttribute("productionList", productionList);
                return new ModelAndView("index","model",model);
            } else if (UserInfo.getState().equals(0)){
                Integer uStyle = UserInfo.getStyle();
                model.addAttribute("uStyle", uStyle);
                return new ModelAndView("validate","model",model);
            } else if (UserInfo.getState().equals(2)){
                return new ModelAndView("waitTip","model",model);
            }else  if (UserInfo.getState().equals(3)){
                return new ModelAndView("failTip","model",model);
            }
        }
        //未登录则跳转登录页面
        return new ModelAndView("login","model",model);
    }
    @GetMapping("/")
    public String getMain(Model model){
        List<Information> informationList = informationService.getInformationListTop(4); //获取前四个咨询
        List<Matchs> matchList = matchService.getMatchListTop(3); //获取前三个比赛
        List<Production> productionList = productionService.getProductionListTop(3); //拿到最新的3个作品
        model.addAttribute("informationList", informationList);
        model.addAttribute("matchList", matchList);
        model.addAttribute("productionList", productionList);
        return "index";
    }


    @GetMapping("/admin")
    public String admin(){
        return "admins/index";
    }
}
