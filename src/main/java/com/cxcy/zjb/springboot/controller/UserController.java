/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: UserController
 * Author:   KOLO
 * Date:     2018/8/18 22:50
 * Description: 用户控制类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.*;
import com.cxcy.zjb.springboot.converter.UserDto;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.service.*;
import com.cxcy.zjb.springboot.utils.ResultUtil;
import com.cxcy.zjb.springboot.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;

/**
 * 〈一句话功能简述〉<br> 
 * 〈查询用户的一些个人信息〉
 *
 * @author KOLO
 * @create 2018/8/18
 * @since 1.0.0
 */
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private GrowthService growthService;

    /**
     * 聊天模块：获取当前用户的id
     * @param session
     * @return
     */
    @RequestMapping("/person")
    @ResponseBody
    public User getUserId(HttpSession session){
        User user = (User)session.getAttribute("user");

        return user;
    }

    /**
     * 查询该用户的未读信息数
     * @param userId
     * @return
     */
    @RequestMapping("/message")
    @ResponseBody
    public Result findUserMessage(@RequestParam(value = "userId",required = true) Integer userId){
        //根据用户ID查询未读信息数
        ArrayList<UserMessage> list = userService.findMessageByUserId(userId+"");

        Result result = ResultUtil.success(list);

        return result;
    }

    /**
     * 保存用户信息
     * @param studentVo
     * @param bindingResult
     * @param session
     * @return
     */
    @RequestMapping("/saveStudent")
    @ResponseBody
    public Result userSaveStudent(@Valid StudentVo studentVo, BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            System.err.println(msg);
            return ResultUtil.error(msg);
        }

        Student student = new Student();
        BeanUtils.copyProperties(studentVo,student);
        student.setNumber(Long.parseLong(studentVo.getNumber()));
        Student saveStudent = studentService.saveStudent(student);

        //将s_id信息和真实姓名存进user表中
        try {
            User userInfo = (User)session.getAttribute("user");
            userInfo.setName(studentVo.getName());
            userInfo.setStudent(saveStudent.getId());
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】session中没有user");
            return ResultUtil.error();
        }

        return ResultUtil.success();
    }

    /**
     * 认证中，重新提交材料
     * @return
     */
    @RequestMapping("/submitAgain")
    @ResponseBody
    public Result submitAgain(HttpSession session){
        User userInfo = null;
        try {
            userInfo = (User) session.getAttribute("user");
            //设置状态为认证中
            userInfo.setState(0);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】session中没有user");
            return ResultUtil.error();
        }
        return ResultUtil.success(userInfo.getStyle());
    }

    /**
     * 保存teacher的认证数据
     * @param teacherVo
     * @param bindingResult
     * @param session
     * @return
     */
    @RequestMapping("/saveTeacher")
    @ResponseBody
    public Result userSaveTeacher(@Valid TeacherVo teacherVo, BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            System.err.println(msg);
            return ResultUtil.error(msg);
        }

        Teacher teacher = new Teacher();
        BeanUtils.copyProperties(teacherVo,teacher);
        teacher.setNumber(Long.parseLong(teacherVo.getNumber()));
        Teacher saveTeacher = teacherService.saveTeacher(teacher);

        //将s_id信息和真实姓名存进user表中
        try {
            User userInfo = (User)session.getAttribute("user");
            userInfo.setName(teacherVo.getName());
            userInfo.setTeacher(saveTeacher.getId());
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】session中没有user");
            return ResultUtil.error();
        }

        return ResultUtil.success();
    }

    /**
     * 保存企业认证信息
     * @param companyVo
     * @param bindingResult
     * @param session
     * @return
     */
    @RequestMapping("/saveCompany")
    @ResponseBody
    public Result userSaveCommany(@Valid CompanyVo companyVo, BindingResult bindingResult, HttpSession session){
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            System.err.println(msg);
            return ResultUtil.error(msg);
        }
        //将图片进行接收
        String image = (String)session.getAttribute("image");

        Company company = new Company();
        BeanUtils.copyProperties(companyVo,company);
        company.setLicense(image);
        Company saveCompany = companyService.saveCompany(company);

        //将s_id信息和真实姓名存进user表中
        try {
            User userInfo = (User)session.getAttribute("user");
            userInfo.setName(companyVo.getRealName());
            userInfo.setCompany(saveCompany.getId());
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】session中没有user");
            return ResultUtil.error();
        }

        return ResultUtil.success();
    }

    @PostMapping(value = "/imageUpload")
    @ResponseBody
    public Result imageUpload(@RequestParam("file") MultipartFile file, HttpSession session){
        //定义图片大小
        long maxSize = 1024*1024*2;
        long size = file.getSize();
        if (size > maxSize){
            return ResultUtil.error("上传图片不能超过2M");
        }
        if (file.isEmpty()){
            log.info("【文件上传】出错了，图片还没上传");
            return ResultUtil.error("图片还没上传！");
        }
        String newFileName = "";
        try {
            //获取文件名称
            String originalFilename = file.getOriginalFilename();
            //编写新名称
            newFileName = UUIDUtil.getUUID()+originalFilename.substring(originalFilename.lastIndexOf("."));
            //将图片放入硬盘
            file.transferTo(new File("F:\\image\\"+newFileName));
            //将新文件名存进session中
            session.setAttribute("image",newFileName);
        }catch (Exception e){
            log.error("【文件上传】出错了！");
        }
        return ResultUtil.success(newFileName);
    }

    /**
     * 保存管理员认证信息
     * @param session
     * @return
     */
    @RequestMapping("/saveAdmin")
    @ResponseBody
    public Result userSaveAdmin(@RequestParam("name") String name, HttpSession session){

        if (name.trim() == ""){
            return ResultUtil.error("真实姓名不能为空");
        }
        //将s_id信息和真实姓名存进user表中
        try {
            User userInfo = (User)session.getAttribute("user");
            userInfo.setName(name);
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】session中没有user");
            return ResultUtil.error();
        }

        return ResultUtil.success();
    }

    /**
     * 通过用户的信息认证
     * @param id
     * @return
     */
    @RequestMapping("/pass")
    @ResponseBody
    public Result passUserIdentification(@RequestParam("id") String  id){
        if (id.trim()==""){
            System.out.println("【用户认证】出错了，id不能为空");
            return ResultUtil.error("id不能为空");
        }
        //修改用户的认证状态
        User user = userService.findUserbyUserId(id);
        user.setState(1);
        User saveUserInfo = userService.saveUserInfo(user);
        if (saveUserInfo==null){
            return ResultUtil.error("【用户认证】出错了，修改用户的认证状态失败！");
        }
        //在growth表增加学生信息
        Growth growth = new Growth();
        growth.setUser(saveUserInfo.getId());
        Growth saveGrowth = growthService.saveGrowth(growth);
        if (saveGrowth == null){
             return ResultUtil.error("【用户认证】出错了，在growth表增加学生信息失败！");
        }

        return ResultUtil.success();
    }

    /**
     * 聊天系统登录账号
     * @param userName
     * @param userPwd
     * @param session
     * @return
     */
    @PostMapping("/login")
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


    @RequestMapping("/login2")
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
    @RequestMapping("/personMessage")
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

