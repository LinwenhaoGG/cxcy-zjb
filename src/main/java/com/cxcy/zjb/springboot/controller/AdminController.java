package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Result;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.CompanyService;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.TeacherService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Map;

/**
 * 管理员控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    MatchService matchService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CompanyService companyService;

    /**
     * 管理员查看所有比赛
     * @param map
     * @return
     */
    @GetMapping("/matchslist")
    public ModelAndView listMatchsAdmin(@RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                        @RequestParam(value = "size", defaultValue = "5") Integer size,//一页个数
                                        Map map) {
        //根据开始时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"StartTime");
        PageRequest request = new PageRequest(page, size,sort);
        //根据排序查出分页
        Page<Matchs> matchsPage = matchService.findAll(request);
        map.put("matchsPage", matchsPage);
        map.put("page", page);
        map.put("size", size);

        return new ModelAndView("matchs/adminsMatchsList", map);
    }

    /**
     * 学生认证：查看未认证的学生信息
     * @return
     */
    @PostMapping("/studentIdentification")
    public Result studentIdentificationList(@RequestParam(value = "page",defaultValue = "1") Integer page ,//当前页
                                            @RequestParam(value = "size",defaultValue = "2") Integer size ){

        try {
             //查找未认证的学生列表
            PageRequest pageRequest = new PageRequest(page - 1 ,size);
            ArrayList pageList =  userService.findStudentIdentificationList(pageRequest);
            return ResultUtil.success(pageList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查询到未认证的学生数据！");
        }

    }

     /**
     * 老师：查看未认证的老师信息
     * @return
     */
    @PostMapping("/teacherIdentification")
    public Result teacherIdentification(@RequestParam(value = "page",defaultValue = "1") Integer page ,//当前页
                                            @RequestParam(value = "size",defaultValue = "2") Integer size ){

        try {

             //查看未认证的老师信息
            PageRequest pageRequest = new PageRequest(page - 1 ,size);
            ArrayList pageList =  teacherService.findTeacherIdentificationList(pageRequest);
            return ResultUtil.success(pageList);

        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查询到未认证的老师数据！");
        }

    }

    /**
     * 企业：查看未认证的企业信息
     * @return
     */
    @PostMapping("/companyIdentification")
    public Result companyIdentification(@RequestParam(value = "page",defaultValue = "1") Integer page ,//当前页
                                            @RequestParam(value = "size",defaultValue = "2") Integer size ){

         try {

             //查看未认证的企业信息
            PageRequest pageRequest = new PageRequest(page - 1 ,size);
            ArrayList pageList =  companyService.findCompanyIdentification(pageRequest);
             return ResultUtil.success(pageList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查询到未认证的企业数据！");
        }



    }

    /**
     * 管理员：查看未认证的管理员信息
     * @return
     */
    @PostMapping("/adminIdentification")
    public Result adminIdentification(@RequestParam(value = "page",defaultValue = "1") Integer page ,//当前页
                                            @RequestParam(value = "size",defaultValue = "3") Integer size ){
         try {

             //查看未认证的管理员信息
            PageRequest pageRequest = new PageRequest(page - 1 ,size);
            ArrayList pageList =  userService.findAdminIdentification(pageRequest);
            return ResultUtil.success(pageList);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查询到未认证的管理员数据！");
        }



    }

    /**
     * 管理员通过认证功能
     * @param id
     * @return
     */
    @PostMapping("/pass")
    public Result passIdentification(@RequestParam(value = "id" ,required = true) Long id){


        try {

            //根据id修改用户的认证状态
            User user =  userService.passUserIdentification(id);
            if (user == null){
                return ResultUtil.error("修改用户认证状态失败！");
            }else {
                return ResultUtil.success("修改成功！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法进行通过认证功能！");
        }
    }

    /**
     * 管理员拒绝通过用户认证功能
     * @param id
     * @return
     */
    @PostMapping("/rejectPass")
    public Result rejectPassIdentification(@RequestParam(value = "id" ,required = true) Long id){


         try {

            //根据id修改用户的认证状态
            User user =  userService.rejectPassUserIdentification(id);
            if (user == null){
                return ResultUtil.error("修改用户认证状态失败！");
            }else {
                return ResultUtil.success("修改成功！");
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法进行拒绝认证功能！");
        }
    }

    /**
     * 学生管理：查找已认证学生list集合
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/certifiedStudent")
    public Result findCertifiedStudent(@RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size" ,defaultValue = "5") int size){

        try {

           //根据查找已认证学生
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = userService.findCertifiedStudent(pageRequest);

            return ResultUtil.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找已认证学生！");
        }
    }

    /**
     * 根据关键字查找指定学生
     * @param selectState
     * @param keyword
     * @return
     */
    @RequestMapping("/findStudent")
    public Result findSingleStudent(@RequestParam(value = "selectState",required = true) int selectState,
                                    @RequestParam(value = "keywork" , required = true) String keyword,
                                    @RequestParam(value = "page",required = false,defaultValue = "1")int page ,
                                    @RequestParam(value = "size",required = false,defaultValue = "5") int size
                                    ){


         try {
           //1，根据关键字查找指定用户
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = userService.findSingleStudent(selectState,keyword,pageRequest);

            if (list!=null){
                return ResultUtil.success(list);
            }else {
                return ResultUtil.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找指定学生！");
        }

    }

    /**
     * 老师管理：查找已认证老师list集合
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/certifiedTeacher")
    public Result findCertifiedTeacher(@RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size" ,defaultValue = "5") int size){

        try {
           //根据查找已认证老师
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = teacherService.findCertifiedTeacher(pageRequest);

            return ResultUtil.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找已认证老师！");
        }
    }

     /**
     * 根据关键字查找指定老师
     * @param selectState
     * @return
     */
    @RequestMapping("/findTeacher")
    public Result findTeacher(@RequestParam(value = "selectState",required = true) int selectState,
                                    @RequestParam(value = "keywork" , required = true) String keyword,
                                    @RequestParam(value = "page",required = false,defaultValue = "1")int page ,
                                    @RequestParam(value = "size",required = false,defaultValue = "5") int size
                                    )
    {

        try {
           //1，根据关键字查找指定用户
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = userService.findTeacher(selectState,keyword,pageRequest);

            if (list!=null){
                return ResultUtil.success(list);
            }else {
                return ResultUtil.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找指定老师！");
        }

    }

     /**
     * 企业管理：查找已认证企业list集合
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/certifiedCompany")
    public Result findCertifiedCompany(@RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size" ,defaultValue = "5") int size){

        try {
            //根据查找已认证企业
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = companyService.findCertifiedTeacher(pageRequest);

            return ResultUtil.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找已认证企业！");
        }

    }

    /**
     * 根据关键字查找指定企业
     * @param selectState
     * @return
     */
    @RequestMapping("/findCompany")
    public Result findCompany(@RequestParam(value = "selectState",required = true) int selectState,
                                    @RequestParam(value = "keywork" , required = true) String keyword,
                                    @RequestParam(value = "page",required = false,defaultValue = "1")int page ,
                                    @RequestParam(value = "size",required = false,defaultValue = "5") int size
                                    )
    {


         try {
            //1，根据关键字查找指定企业用户
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = userService.findCompany(selectState,keyword,pageRequest);

            if (list!=null){
                return ResultUtil.success(list);
            }else {
                return ResultUtil.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找指定企业！");
        }
    }

    /**
     * 管理员管理：查找已认证管理员list集合
     * @param page
     * @param size
     * @return
     */
    @PostMapping("/certifiedAdmin")
    public Result findCertifiedAdmin(@RequestParam(value = "page",defaultValue = "1") int page,
                                       @RequestParam(value = "size" ,defaultValue = "5") int size){

         try {
            //根据查找已认证管理员
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = userService.findCertifiedAdmin(pageRequest);

            return ResultUtil.success(list);
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("暂时无法查找已认证管理员！");
        }

    }

    /**
     * 根据关键字查找指定管理员
     * @param selectState
     * @return
     */
    @RequestMapping("/findAdmin")
    public Result findAdmin(@RequestParam(value = "selectState",required = true) int selectState,
                                    @RequestParam(value = "keywork" , required = true) String keyword,
                                    @RequestParam(value = "page",required = false,defaultValue = "1")int page ,
                                    @RequestParam(value = "size",required = false,defaultValue = "5") int size
                                    )
    {


         try {
            //1，根据关键字查找指定管理员
            PageRequest pageRequest = new PageRequest(page - 1,size);
            ArrayList list = userService.findAdmin(selectState,keyword,pageRequest);

            if (list!=null){
                return ResultUtil.success(list);
            }else {
                return ResultUtil.error();
            }
        }catch (Exception e){
            e.printStackTrace();
            return ResultUtil.error("无法查找该指定管理员！");
        }
    }

}
