package com.cxcy.zjb.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页控制层
 *
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String getMain(){
        return "index";
    }
}
