/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: RegisterController
 * Author:   KOLO
 * Date:     2018/8/24 16:42
 * Description: 注册账号控制器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.controller;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.cxcy.zjb.springboot.Vo.Result;
import com.cxcy.zjb.springboot.Vo.UserRegister;
import com.cxcy.zjb.springboot.domain.Authority;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtil;
import com.cxcy.zjb.springboot.utils.SendSmsUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈注册账号控制器〉
 *
 * @author KOLO
 * @create 2018/8/24
 * @since 1.0.0
 */
@Controller
public class RegisterController {

    @Autowired
    private UserService userInfoService;

    /**
     * 给指定的手机号码发送手机验证码
     * @param telephone
     */
    @RequestMapping("user/sendSms")
    @ResponseBody
    public Result sendSms(@RequestParam(value = "telephone",required = false) String telephone, HttpSession session){
        SendSmsResponse response = null;
        try {
            String randomCode = SendSmsUtil.getRandomCode();
            session.setAttribute("randomCode",randomCode);
            response= SendSmsUtil.sendSms(telephone,randomCode);
        }catch (Exception e){
            e.printStackTrace();
        }
        Result result = ResultUtil.success(response.getCode());
        return result;
    }

    @RequestMapping("/user/register")
    @ResponseBody
    public Result register(@Valid UserRegister register, BindingResult bindingResult, HttpSession session){

        List<Authority> authorities = new ArrayList<>();
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            System.out.println(msg);
            return ResultUtil.error(msg);
        }
        //1,验证验证码是否正确
        String randomCode = (String)session.getAttribute("randomCode");
        if (!randomCode.equalsIgnoreCase(register.getCode())){
            return ResultUtil.error("验证码错误！");
        }
        session.setAttribute("randomCode","");
        //2,将信息存数据库
        User userInfo = new User();
        BeanUtils.copyProperties(register,userInfo);
        userInfo.setAuthorities(authorities);
        userInfo.setStyle(Integer.parseInt(register.getStyle()));
        //若该用户为管理员，则直接将认证状态改为认证中
        if (register.getStyle().equalsIgnoreCase("4")){
            userInfo.setState(2);
        }
        //3,将数据存数据库
        User info = userInfoService.saveUserInfo(userInfo);

        Result success = ResultUtil.success(info);

        return success;
    }

    /**
     * 查找账号是否已经存在
     * @return
     */
    @RequestMapping("user/key")
    @ResponseBody
    public Result findKey(@RequestParam(value = "uName",required = true) String uName){
        //1,根据用户名查询是否已经存在
        User userInfo = userInfoService.findUserInfo(uName);
        //2,返回数据
        Result result = null;
        if (userInfo == null){
            result = ResultUtil.success();
        }else {
            result =  ResultUtil.error("账号已存在");
        }
        return result;
    }

}