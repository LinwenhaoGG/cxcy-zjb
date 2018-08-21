package com.cxcy.zjb.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 比赛控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/match")
public class MatchController {

    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "/match/index";
    }


    @RequestMapping("/a")
    public ModelAndView listUsers() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/match/wysiwyg");
        return mav;
    }
}
