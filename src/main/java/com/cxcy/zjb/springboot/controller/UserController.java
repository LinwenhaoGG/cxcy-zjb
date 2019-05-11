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

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.cxcy.zjb.springboot.Vo.*;
import com.cxcy.zjb.springboot.constants.UserContants;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.enums.ResultEnum;
import com.cxcy.zjb.springboot.service.*;
import com.cxcy.zjb.springboot.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.MD5;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    @Autowired
    ProductionService productionService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * 获取当前登录的用户
     * @return
     */
    @RequestMapping("/person")
    @ResponseBody
    public Object getUserId(){
        //获取当前登录的用户
        return  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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
     * @return
     */
    @RequestMapping("/saveStudent")
    @ResponseBody
    public Result userSaveStudent(@Valid StudentVo studentVo, BindingResult bindingResult){
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
            User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userInfo.setName(studentVo.getName());
            userInfo.setStudent(saveStudent.getId());
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】没有user登录");
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
    public Result submitAgain(){
        User userInfo = null;
        try {
            //获取当前登录的用户
            userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            //设置状态为认证中
            userInfo.setState(0);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】没有用户登录");
            return ResultUtil.error();
        }
        return ResultUtil.success(userInfo.getStyle());
    }

    /**
     * 保存teacher的认证数据
     * @param teacherVo
     * @param bindingResult
     * @return
     */
    @RequestMapping("/saveTeacher")
    @ResponseBody
    public Result userSaveTeacher(@Valid TeacherVo teacherVo, BindingResult bindingResult){
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
       // try {
            //获取当前登录的用户
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User userInfo = userService.findUserById(user.getId());
            userInfo.setName(teacherVo.getName());
            userInfo.setTeacher(saveTeacher.getId());
            //设置状态为认证中
            userInfo.setState(UserContants.STATE_IN_AUDIT);
            userService.saveUserInfo(userInfo);
//        }catch (Exception e){
//            log.info("【出错啦】web中没有user登录");
//            log.info(e.getMessage());
//            return ResultUtil.error();
//        }

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
            //获取当前登录用户
            User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userInfo.setName(companyVo.getRealName());
            userInfo.setCompany(saveCompany.getId());
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】web中没有user登录");
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
            file.transferTo(new File("D:\\upload\\image\\"+newFileName));
            //将新文件名存进session中
            session.setAttribute("image",newFileName);
        }catch (Exception e){
            log.error("【文件上传】出错了！");
        }
        return ResultUtil.success(newFileName);
    }

    /**
     * 保存管理员认证信息
     * @return
     */
    @RequestMapping("/saveAdmin")
    @ResponseBody
    public Result userSaveAdmin(@RequestParam("name") String name){

        if (name.trim() == ""){
            return ResultUtil.error("真实姓名不能为空");
        }
        //将s_id信息和真实姓名存进user表中
        try {
            //获取登录中的用户
            User userInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            userInfo.setName(name);
            //设置状态为认证中
            userInfo.setState(2);
            userService.saveUserInfo(userInfo);
        }catch (Exception e){
            log.info("【出错啦】web中没有user登录");
            return ResultUtil.error();
        }

        return ResultUtil.success();
    }

    /**
     * 获取聊天列表
     * @return
     */
    @GetMapping("/friendList")
    public String friendList() {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            return "/login";
        }
        return "users/friendList";
    }
    /**
     * 查询用户个人信息
     * @return
     */
    @RequestMapping("/personMessage")
    @ResponseBody
    public Result userPersonMessage(){
        try {
            User userInfo =(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResultUtil.success(userInfo.getUsername());
        }catch (Exception e){
            System.err.println("【出错了】userPersonMessage -> 用户不在sesession中");
        }
        return ResultUtil.error();
    }

    /**
     * 给指定的手机号码发送手机验证码
     * @param telephone
     */
    @RequestMapping("/sendSms")
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

    /**
     * 验证注册
     * @param register
     * @param bindingResult
     * @param session
     * @return
     */
    @RequestMapping("/register")
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
        userInfo.setPassword(encoder.encode(register.getPassword()));
        //若该用户为管理员，则直接将认证状态改为认证中
        if (register.getStyle().equalsIgnoreCase("4")){
            userInfo.setState(2);
        }
        //3,将数据存数据库
        User info = userService.saveUserInfo(userInfo);

        Result success = ResultUtil.success(info);

        return success;
    }

    /**
     * 查找账号是否已经存在
     * @return
     */
    @RequestMapping("/key")
    @ResponseBody
    public Result findKey(@RequestParam(value = "uName",required = true) String uName){
        //1,根据用户名查询是否已经存在
        User userInfo = userService.findUserInfo(uName);
        //2,返回数据
        Result result = null;
        if (userInfo == null){
            result = ResultUtil.success();
        }else {
            result =  ResultUtil.error("账号已存在");
        }
        return result;
    }

    /**
     * 根据学生用户id获取用户详情
     * @param id
     * @return
     */
    @RequestMapping("/showUserGrowth")
    public String showUserGrowth(Long id, Model model) {
        UserStudentVo userStudentVo = studentService.findById(id);
        //如果用户信息不是为学生
        if (userStudentVo == null || !userStudentVo.getStyle().equals(UserContants.STYLE_STUDENT)) {
            return "/index";
        }
        Growth growth = growthService.findByUser(userStudentVo.getSId());
        Integer productCount = productionService.getProductCountByUser(id);

        model.addAttribute("student", userStudentVo);
        model.addAttribute("growth", growth);
        model.addAttribute("productCount", productCount);

        return "/users/student_detail";
    }

    /**
     * 学生修改基本信息
     * @return
     */
    @GetMapping("/userUpdateMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_COMPANY','ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public String userUpdateMsg(Model model) {
        User user = UserUtils.getUser();
        if (user.getStyle().equals(UserContants.STYLE_STUDENT)) {
            UserStudentVo userStudentVo = studentService.findById(user.getId());
            Growth growth = growthService.findByUser(userStudentVo.getSId());
            model.addAttribute("student", userStudentVo);
            model.addAttribute("growth", growth);
            return "personalInformation/studentInformation";
        } else if (user.getStyle().equals(UserContants.STYLE_COMAPNY)) {
            UserCompanyVo userCompanyVo = companyService.findUserCompanyById(user.getId());
            model.addAttribute("company", userCompanyVo);
            return "personalInformation/businessInformation";
        } else if (user.getStyle().equals(UserContants.STYLE_TEACHER)) {
            UserTeacherVo userTeacherVo = teacherService.findTeacherById(user.getId());
            model.addAttribute("teacher", userTeacherVo);
            return "personalInformation/teacherInfomation";
        }
        return "redirect:/login";
    }

    /**
     * 学生修改基本信息
     * @return
     */
    @PostMapping("/studentUpdateMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result studentUpdateMsg(UserStudentVo student) {
        User user = UserUtils.getUser();
        user.setSex(student.getSex());
        user.setEmail(student.getEmail());
        Student oldStudent = studentService.getStudent(user.getStudent());
        oldStudent.setClasses(student.getClasses());
        oldStudent.setEdu(student.getEdu());
        oldStudent.setNation(student.getNation());
        oldStudent.setPoliticsstatus(student.getPoliticsstatus());
        oldStudent.setCredential(student.getCredential());
        userService.saveUserInfo(user);
        studentService.saveStudent(oldStudent);
        return ResultUtil.success();
    }

    /**
     * 企业修改基本信息
     * @return
     */
    @PostMapping("/companyUpdateMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result companyUpdateMsg(Company newCompany) {
        User user = UserUtils.getUser();
        Company company = companyService.getCompany(user.getCompany());
        company.setContacts(newCompany.getContacts());
        company.setPhone(newCompany.getPhone());
        companyService.saveCompany(company);
        return ResultUtil.success();
    }

    /**
     * 教师修改基本信息
     * @return
     */
    @PostMapping("/teacherUpdateMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result teacherUpdateMsg(UserTeacherVo userTeacherVo) {
        User user = UserUtils.getUser();
        Teacher teacher = teacherService.getTeacher(user.getTeacher());
        teacher.setCredential(userTeacherVo.getCredential());
        teacher.setSchool(userTeacherVo.getSchool());
        teacher.setSpecially(userTeacherVo.getSpecially());
        teacher.setCollege(userTeacherVo.getCollege());
        teacher.setNation(userTeacherVo.getNation());
        teacher.setPosition(userTeacherVo.getPosition());
        teacher.setPoliticsstatus(userTeacherVo.getPoliticsstatus());
        teacherService.saveTeacher(teacher);
        return ResultUtil.success();
    }

        //------------------------------------------------
      //            管理员端接口
    //------------------------------------------

    /**
     * 通过学生用户的信息认证
     * @param id
     * @return
     */
    @RequestMapping("/passStudent")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    @Transactional
    public Result passUserStudent(@RequestParam("id") String id){
        if (StringUtils.isEmpty(id)){
            System.out.println("【用户认证】出错了，id不能为空");
            return ResultUtil.error("id不能为空");
        }
        //修改用户的认证状态
        User user = userService.findUserbyUserId(id);
        if (user == null){
            return ResultUtil.error("【用户认证】出错了，修改用户的认证状态失败！");
        }
        user.setState(UserContants.STATE_IS_ACCEPT);
        User saveUserInfo = userService.saveUserInfo(user);
        userService.giveUserAuthority(user.getId(), UserContants.ROLE_STUDENT_ID);
        //在growth表增加学生信息
        Growth growth = new Growth();
        growth.setUser(saveUserInfo.getStudent());
        Growth saveGrowth = growthService.saveGrowth(growth);
        if (saveGrowth == null){
            return ResultUtil.error("【用户认证】出错了，在growth表增加学生信息失败！");
        }
        return ResultUtil.success();
    }

    /**
     * 通过教师用户的信息认证
     * @param id
     * @return
     */
    @RequestMapping("/passTeacher")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    @Transactional
    public Result passUserTeacher(@RequestParam("id") String id){
        if (StringUtils.isEmpty(id)){
            System.out.println("【用户认证】出错了，id不能为空");
            return ResultUtil.error("id不能为空");
        }
        //修改用户的认证状态
        User user = userService.findUserbyUserId(id);
        if (user == null){
            return ResultUtil.error("【用户认证】出错了，修改用户的认证状态失败！");
        }
        user.setState(UserContants.STATE_IS_ACCEPT);
        User saveUserInfo = userService.saveUserInfo(user);
        userService.giveUserAuthority(user.getId(), UserContants.ROLE_TEACHER_ID);
        return ResultUtil.success();
    }

    /**
     * 通过公司用户的信息认证
     * @param id
     * @return
     */
    @RequestMapping("/passCompany")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    @Transactional
    public Result passCompany(@RequestParam("id") String id){
        if (StringUtils.isEmpty(id)){
            System.out.println("【用户认证】出错了，id不能为空");
            return ResultUtil.error("id不能为空");
        }
        //修改用户的认证状态
        User user = userService.findUserbyUserId(id);
        if (user == null){
            return ResultUtil.error("【用户认证】出错了，修改用户的认证状态失败！");
        }
        user.setState(UserContants.STATE_IS_ACCEPT);
        User saveUserInfo = userService.saveUserInfo(user);
        userService.giveUserAuthority(user.getId(), UserContants.ROLE_COMPANY_ID);
        return ResultUtil.success();
    }

    /**
     * 拒绝用户的信息认证
     * @param id
     * @return
     */
    @RequestMapping("/refuseUser")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result refuseUserIdentification(@RequestParam("id") String  id){
        if (StringUtils.isEmpty(id)){
            System.out.println("【用户认证】出错了，id不能为空");
            return ResultUtil.error("id不能为空");
        }
        //修改用户的认证状态
        User user = userService.findUserbyUserId(id);
        if (user == null){
            return ResultUtil.error("【用户认证】出错了，修改用户的信息不存在！");
        }
        user.setState(UserContants.STATE_IS_REFUSE);
        User saveUserInfo = userService.saveUserInfo(user);

        if (saveUserInfo == null){
            return ResultUtil.error("【用户认证】出错了，在修改用户认证状态时出错了！");
        }
        return ResultUtil.success();
    }

    /**
     * 显示学生的基本信息
     * @param id
     * @return
     */
    @RequestMapping("/getStudentMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getUserMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        UserStudentVo userStudentVo = studentService.findById(id);
        model.addAttribute("student", userStudentVo);
        return "admins/pages/manage/user/show/student-show";
    }

    /**
     * 显示教师的基本信息
     * @param id
     * @return
     */
    @RequestMapping("/getTeacherMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getTeacherMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        UserTeacherVo userTeacherVo = teacherService.findTeacherById(id);
        model.addAttribute("teacher", userTeacherVo);
        return "admins/pages/manage/user/show/teacher-show";
    }

    /**
     * 显示公司的基本信息
     * @param id
     * @return
     */
    @RequestMapping("/getCompanyMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getCompanyMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        UserCompanyVo userCompanyVo = companyService.findUserCompanyById(id);
        model.addAttribute("company", userCompanyVo);
        return "admins/pages/manage/user/show/company-show";
    }

    /**
     * 显示学生列表，可根据学生姓名
     * @param keyword  学生姓名关键字
     * @return
     */
    @RequestMapping("/getStudentListByName")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getUserStudentListByPersonName(String keyword, Model model) {
        List<UserStudentVo> userStudentVos = studentService.findStudentList(keyword);
        model.addAttribute("studentList", userStudentVos);
        model.addAttribute("keyword", keyword);
        return "admins/pages/manage/user/studentManage";
    }

    /**
     * 显示教师列表，可根据教师姓名
     * @param keyword  教师姓名关键字
     * @return
     */
    @RequestMapping("/getTeacherListByName")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getTeacherListByName(String keyword, Model model) {
        List<UserTeacherVo> userTeacherVos = teacherService.findTeacherListByName(keyword);
        model.addAttribute("teacherList", userTeacherVos);
        model.addAttribute("keyword", keyword);
        return "admins/pages/manage/user/teacherManage";
    }

    /**
     * 显示公司列表，可根据公司名称
     * @param keyword  公司名称关键字
     * @return
     */
    @RequestMapping("/getCompanyListByName")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getCompanyListByName(String keyword, Model model) {
        List<UserCompanyVo> userCompanyVos = companyService.findCompanyListByName(keyword);
        model.addAttribute("companyList", userCompanyVos);
        model.addAttribute("keyword", keyword);
        return "admins/pages/manage/user/companyManage";
    }

    /**
     * 显示管理员列表，可根据管理员名称
     * @param keyword  公司名称关键字
     * @return
     */
    @RequestMapping("/getAdminListByName")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")  // 指定角色权限才能操作方法
    public String getAdminListByName(String keyword, Model model) {
        List<User> userList = userService.findUserListByStyle(4);  //根据管理员类型查找出对应的信息
        model.addAttribute("adminList", userList);
        model.addAttribute("keyword", keyword);
        return "admins/pages/manage/user/adminManage";
    }

    /**
     * 修改页面显示学生的基本信息
     * @param id
     * @return
     */
    @GetMapping("/updateStudentMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String updateStudentMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        UserStudentVo userStudentVo = studentService.findById(id);
        model.addAttribute("student", userStudentVo);
        return "admins/pages/manage/user/alter/studentAlter";
    }

    /**
     * 修改页面保存学生的基本信息
     * @return
     */
    @PostMapping("/updateStudentMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updateStudent(Student student) {

        studentService.saveStudent(student);
        return ResultUtil.success();
    }

    /**
     * 修改页面显示教师的基本信息
     * @param id
     * @return
     */
    @GetMapping("/updateTeacherMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String updateTeacherMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        UserTeacherVo userTeacherVo = teacherService.findTeacherById(id);
        model.addAttribute("teacher", userTeacherVo);
        return "admins/pages/manage/user/alter/teacherAlter";
    }

    /**
     * 修改页面保存教师的基本信息
     * @return
     */
    @PostMapping("/updateTeacherMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updateTeacher(Teacher teacher) {

        teacherService.saveTeacher(teacher);
        return ResultUtil.success();
    }

    /**
     * 修改页面显示公司的基本信息
     * @param id
     * @return
     */
    @GetMapping("/updateCompanyMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String updateCompanyMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        UserCompanyVo userCompanyVo = companyService.findUserCompanyById(id);
        model.addAttribute("company", userCompanyVo);
        return "admins/pages/manage/user/alter/companyAlter";
    }

    /**
     * 修改页面保存公司的基本信息
     * @return
     */
    @PostMapping("/updateCompanyMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updateCompany(Company company) {

        companyService.saveCompany(company);
        return ResultUtil.success();
    }

    /**
     * 修改页面显示管理员的基本信息
     * @param id
     * @return
     */
    @GetMapping("/updateAdminMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")  // 指定角色权限才能操作方法
    public String updateAdminMsg(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        User user = userService.findUserById(id);
        model.addAttribute("admin", user);
        return "admins/pages/manage/user/alter/adminAlter";
    }

    /**
     * 修改页面保存管理员的基本信息
     * @return
     */
    @PostMapping("/updateAdminMsg")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updateAdmin(User user) {
        if (null != user && user.getId() != null) {
            User oldUser = userService.findUserById(user.getId());
            oldUser.setSex(user.getSex());
            oldUser.setName(user.getName());
            userService.saveUserInfo(oldUser);
        }
        return ResultUtil.success();
    }

    /**
     * 跳转到修改管理员个人信息页面
     * @return
     */
    @GetMapping("/updatePersonalAdmin")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN')")  // 指定角色权限才能操作方法
    public String toUpdatePersonalAdmin(Model model) {
        //判断是否已经登录
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User UserInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("admin", UserInfo);
            return "admins/adminInformation";
        }
        return "/login";
    }

    /**
     * 管理员修改个人信息
     * @return
     */
    @PostMapping("/updatePersonalAdmin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updatePersonalAdmin(User user) {

        if (null != user && user.getId() != null) {
            //判断是否已经登录
            if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                    && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
                User UserInfo = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!UserInfo.getId().equals(user.getId())) {  //如果当前的用户与修改的管理员用户不是同一个
                    return ResultUtil.error();
                }
                User oldUser = userService.findUserById(user.getId());
                oldUser.setSex(user.getSex());
                oldUser.setName(user.getName());
                oldUser.setPassword(encoder.encode(user.getPassword()));

                userService.saveUserInfo(oldUser);
            }
        }
        return ResultUtil.success();
    }

    /**
     * 修改用户密码页面
     * @param id
     * @return
     */
    @GetMapping("/updateUserPassword")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String toUpdateStudentPassword(Long id, Model model) {
        //id为空则跳转到错误页面
        if (null == id) {
            model.addAttribute("errMsg", ResultEnum.ID_ISNULL.getMessage());
        }
        User user = userService.findUserById(id);
        model.addAttribute("id", id);
        model.addAttribute("name", user.getName());
        return "admins/pages/manage/user/change-password";
    }

    /**
     * 修改用户的密码
     * @param id
     * @return
     */
    @PostMapping("/updateUserPassword")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updateUserPassword(Long id, String newpassword) {
        //id为空则跳转到错误页面
        if (null == id || StringUtils.isEmpty(newpassword)) {
            return ResultUtil.error();
        }
        User user = userService.findUserById(id);
        if (null != user) {
            user.setPassword(encoder.encode(newpassword));
            userService.saveUserInfo(user);
        }
        return ResultUtil.success();
    }

    /**
     * 修改用户的启用状态
     * @param id
     * @return
     */
    @PostMapping("/updateIsUse")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Result updateIsUse(Long id) {
        //id为空则跳转到错误页面
        if (null == id) {
            return ResultUtil.error();
        }
        User user = userService.findUserById(id);
        if (null != user) {
            if (1 == user.getIsUse()) { //如果为启用状态，则设置为禁用状态
                user.setIsUse(0);
            } else {
                user.setIsUse(1);
            }
            userService.saveUserInfo(user);
            return ResultUtil.success();
        }
        return ResultUtil.error();
    }

    /**
     * 用户统计页面
     * @return
     */
    @GetMapping("/userStatisticsCharts")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String toUserStatisticsCharts(Model model) {
        return "admins/pages/charts/user_statistics_charts";
    }

    /**
     * 用户统计数据
     * @return
     */
    @PostMapping("/userStatisticsCharts")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public Map userStatisticsCharts() {
        Map<String,Object> map = new HashMap<>();
        String title = "用户类型扇形图";
        String type = "用户个数";
        List<UserChartsVo> userChartsVos = userService.getUserChartsCount();
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<String> styleNameList = new ArrayList<>();
        List<Long> countList = new ArrayList<>();
        if (null != userChartsVos) {
            for (UserChartsVo userChartsVo : userChartsVos) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("name", userChartsVo.getName());
                map1.put("y", userChartsVo.getCount());
                mapList.add(map1);
                styleNameList.add(userChartsVo.getName());
                countList.add(userChartsVo.getCount());
            }
        }
        map.put("title", title);
        map.put("type", type);
        map.put("list",mapList);
        map.put("nameList", styleNameList);
        map.put("countList", countList);
        return map;
    }
}

