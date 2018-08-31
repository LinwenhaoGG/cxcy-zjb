package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 用户控制层
 *
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class UserController {
    /**
     * 聊天模块：获取当前用户的id
     * @param session
     * @return
     */
    @RequestMapping("user/person")
    @ResponseBody
    public User getUserId(HttpSession session){
        User user = (User)session.getAttribute("user");

        return user;
    }
}
