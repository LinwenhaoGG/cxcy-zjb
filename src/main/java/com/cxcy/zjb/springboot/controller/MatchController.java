package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 比赛控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/match")
public class MatchController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private MatchService matchService;

}
