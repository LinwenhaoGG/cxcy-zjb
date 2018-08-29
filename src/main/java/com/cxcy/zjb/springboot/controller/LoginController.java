/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: LoginController
 * Author:   KOLO
 * Date:     2018/8/16 17:09
 * Description: 登录
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Result;
import com.cxcy.zjb.springboot.converter.UserDto;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 〈一句话功能简述〉<br> 
 * 〈登录〉
 *
 * @author KOLO
 * @create 2018/8/16
 * @since 1.0.0
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 聊天系统登录账号
     * @param userName
     * @param userPwd
     * @param session
     * @return
     */
    @PostMapping("/user/login")
    public String userLogin(@RequestParam(value = "userName",required = true) String userName, @RequestParam(value = "userPwd",required = true) String userPwd,
                            HttpSession session){
        //1,根据用户名和密码查询用户
        User user = new User();
        user.setUsername(userName);
        user.setPassword(userPwd);

        User user1 = userService.findUser(user);
        if (user1 == null){
            return "redirect:/html/error.html";
        }else {
            session.setAttribute("user",user1);
            return "redirect:/html/friendList.html";
        }
    }


    @RequestMapping("user/login2")
    @ResponseBody
    public Result userLogin2(@RequestParam(value = "userName",required = true) String uName, @RequestParam(value = "userPwd",required = true) String uPassword, HttpSession session){

        User userInfo = userService.findUserInfoByNameAndPwd(uName, uPassword);
        if (userInfo != null){
            session.setAttribute("user",userInfo);
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userInfo,userDto);
            return ResultUtil.success(userDto);
        }else {
            return ResultUtil.error("账号或者密码错误！");
        }
    }

    /**
     * 查询用户个人信息
     * @return
     */
    @RequestMapping("user/personMessage")
    @ResponseBody
    public Result userPersonMessage(HttpSession session){
        try {
            User userInfo =(User)session.getAttribute("user");
            return ResultUtil.success(userInfo.getUsername());
        }catch (Exception e){
            System.err.println("【出错了】userPersonMessage -> 用户不在sesession中");
        }
        return ResultUtil.error();
    }


}