package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Response;
import com.cxcy.zjb.springboot.Vo.Result;
import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.*;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
import com.cxcy.zjb.springboot.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private InformationCategoryService informationCategoryService;

    @Autowired
    private InformationService informationService;
    /**
     * 管理员查看所有比赛
     * @param map
     * @return
     */
    @GetMapping("/matchslist")
    public ModelAndView listMatchsAdmin(@RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                        @RequestParam(value = "size", defaultValue = "5") Integer size,//一页个数
                                        @RequestParam(value = "keyword",defaultValue = "")String keyword,//搜索关键词
                                        Map map) {
        //根据最新发布时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"StartTime");
        PageRequest request = new PageRequest(page - 1, size,sort);
        //根据排序查出分页
        Page<Matchs> matchsPage = matchService.findByNameLike(keyword, request);


        map.put("matchsPage", matchsPage);
        map.put("page", page);
        map.put("size", size);
        map.put("keyword", keyword);
        if (matchsPage.getTotalPages() == 0) {
            map.put("isnull", 1);
        }else {
            map.remove("isnull");
        }

        return new ModelAndView("admins/pages/manage/competition/competition_list", map);
    }

    /**
     * 根据比赛id跳转到对应的比赛编辑页面
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/matchsEdit/{id}")
    public ModelAndView matchEdit(@PathVariable(value = "id") long id, Map map) {
        Matchs matchs = matchService.getMatchById(id);
        map.put("matchs", matchs);
        return new ModelAndView("admins/pages/manage/competition/competition_alter",map);
    }

    /**
     * 保存比赛对象
     * @param matchs
     * @return
     */
    @PostMapping("/saveMatchs")
    public String matchSave(Matchs matchs) {
        //通过id获取比赛类
        Matchs savemaths = matchService.getMatchById(matchs.getId());
        //更新比赛信息
        savemaths.setName(matchs.getName());
        savemaths.setStartTime(matchs.getStartTime());
        savemaths.setLastsigntime(matchs.getLastsigntime());
        savemaths.setLastsubmittime(matchs.getLastsubmittime());
        savemaths.setOverTime(matchs.getOverTime());
        savemaths.setContent(matchs.getContent());
        savemaths.setHtmlContent(matchs.getHtmlContent());
        //保存更新后的比赛信息
        matchService.saveMatch(savemaths);
        //返回比赛列表
        return "redirect:/admins/matchslist";
    }

    @GetMapping("/addmatchs")
    public ModelAndView addMathcs(Map map) {

        //新建一个初始化对象
        Matchs matchs = new Matchs();
        matchs.setOverTime(new Date());
        matchs.setStartTime(new Date());
        matchs.setLastsubmittime(new Date());
        matchs.setLastsigntime(new Date());
        //通过类型查找用户列表，2为教师
        List<User> userList = userService.findUserListByStyle(2);
        //将列表加入
        map.put("userList",userList);
        map.put("matchs",matchs);

        return new ModelAndView("admins/pages/manage/competition/competition_release",map);
    }

    @GetMapping("/")
    public String index() {
        return "admins/index";
    }

    @GetMapping("/test")
    public String test() {
        return "admins/test";
    }

    /**
     * 管理员查看所有资讯类别
     * @return
     */
    @GetMapping("/toInformationCategorieList")
    public String toInformationCategorieList(@RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                @RequestParam(value = "size", defaultValue = "5") Integer size,//一页个数
                                Model model) {
        PageRequest request = new PageRequest(page - 1, size);
        Page<InformationCategory> informationCategories = informationCategoryService.findAllByPage(request);
        model.addAttribute("informationCategories", informationCategories);
        return "admins/pages/news/news_classify";
    }

    /**
     * 管理员查看所有资讯类别
     * @return
     */
    @GetMapping("/toInformationList")
    public String toInformation(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                Model model) {
//        PageRequest request = new PageRequest(page - 1, size);
//        Page<InformationCategory> informationCategories = informationCategoryService.findAllByPage(request);
        List<Information> informationList = informationService.findByTitleLike(keyword);
        model.addAttribute("informationList", informationList);
        return "admins/pages/news/news_information";
    }

    @GetMapping("/informationAdd")
    public String informationAdd(Model model) {
        //获取所有资讯分类
        List<InformationCategory> categories = informationCategoryService.listInformationCategory();
        Information information = new Information(null,null,null);
        model.addAttribute("information", information);
        model.addAttribute("categories",categories);
        return "admins/pages/news/news_add";
    }

    @GetMapping("/informationUpdate")
    public String informationUpdate(@RequestParam("id") Long id,
                                    Model model) {
        //获取所有资讯分类
        List<InformationCategory> categories = informationCategoryService.listInformationCategory();
        Information information = informationService.getInformationById(id);
        model.addAttribute("information", information);
        model.addAttribute("categories",categories);
        return "admins/pages/news/news_add";
    }

    /**
     * 保存资讯(增加或者修改资讯)
     * @param information
     * @return
     */
    @PostMapping("/saveInformation")
    //@PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveOrUpdateInformation( @RequestBody Information information) {
        // 对 Catalog 进行空处理
        if (information.getInformationCategory().getId() == null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {

            // 判断是修改还是新增

            if (information.getId()!=null) {
                Information originalInformation = informationService.getInformationById(information.getId());
                originalInformation.setTitle(information.getTitle());
                originalInformation.setContent(information.getContent());
                originalInformation.setHtmlContent(information.getHtmlContent());
                originalInformation.setAuthor(information.getAuthor());
                originalInformation.setInformationCategory(information.getInformationCategory());
                informationService.saveInformation(originalInformation);
            } else {
                //获取当前登录的用户
                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                information.setUser(user.getId());
                informationService.saveInformation(information);
            }

        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/admins/toInformationList";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
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
