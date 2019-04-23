package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ChartsValueCountVo;
import com.cxcy.zjb.springboot.Vo.Response;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.constants.UserContants;
import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.InformationCategoryService;
import com.cxcy.zjb.springboot.service.InformationService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import com.cxcy.zjb.springboot.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 资讯控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private InformationCategoryService informationCategoryService;

    @Autowired
    private UserService userService;

    /**
     * 返回资讯主页
     * @return
     */
    @GetMapping("/")
    public String informationIndex(){
        return "/information/index";
    }

    @GetMapping("/showTop8")
//    @ResponseBody
    public String getTop8Notice(@RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "8") int pageSize,
                                Model model){
        //根据开始时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"CreateTime");
        Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
        Page<Information> page = informationService.findAllByPage(pageable);
        List<Information> list = page.getContent();
        model.addAttribute("informationList",list);
        return "index :: #informationShowByCategory";

    }

    @GetMapping("/showOneInformation/{id}")
    public String showOneInformation(@PathVariable("id") Long id,Model model){
        Information information = informationService.getInformationById(id);
        informationService.readingIncrease(id);
        User user = userService.findUserById(information.getUser());
        model.addAttribute("informationModel",information);
        model.addAttribute("user",user);
        return "/information/showOne";
    }

    /**
     * 跳转所有资讯首页
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/showAllNotice")
    public String showAllNotice(@RequestParam(name ="catagory",defaultValue = "0") Long catagory,
                                @RequestParam(name = "keyword",defaultValue = "") String keyword,
                                @RequestParam(value = "pageIndex",required = false,defaultValue = "1") int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                Model model){
        //根据开始时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"CreateTime");
        Pageable pageable = new PageRequest(pageIndex-1,pageSize,sort);
        Page<Information> page;
        if (catagory != 0) {//如果是按分类查找
            //找出该分类
            InformationCategory informationCategory = informationCategoryService.getInformationCategoryById(catagory);
            page = informationService.listInformationOrderByCreateTimeDesc(informationCategory, pageable);
        }else {//如果是按所有或查询查找
            page = informationService.findByTitleLike(keyword, pageable);
        }
        List<Information> list = page.getContent();
        model.addAttribute("informationList",list);
        model.addAttribute("keyword",keyword);
        model.addAttribute("catagory",catagory);
        model.addAttribute("page",page);
        model.addAttribute("pageIndex",pageIndex);
        model.addAttribute("pageSize",pageSize);

        return "/information/InformationAll";
    }

    /**
     * 教师管理自己的资讯发布
     * @param username
     * @param order
     * @param categoryId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER','ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String listInformationByOrder(@PathVariable("username") String username,
                                         @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                         @RequestParam(value = "category",required = false,defaultValue = "0") Long categoryId,
                                         @RequestParam(value = "keyword", required = false,defaultValue = "")String keyword,
                                         @RequestParam(value = "async",required = false) boolean async,
                                         @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                         Model model){
        //获取当前登录的用户
        User user = UserUtils.getUser();

        Page<Information> page = null;

        if(categoryId != null && categoryId>0){
            InformationCategory informationCategory = informationCategoryService.getInformationCategoryById(categoryId);
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = informationService.listInformationByCategory(user.getId(),informationCategory,pageable);
        }else if (order.equals("hot")){
            Sort sort = new Sort(Sort.Direction.DESC,"readSize");
            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = informationService.listInformationByTitleVoteAndSort(user.getId(),keyword,pageable);
        }else if (order.equals("new")){
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = informationService.listInformationByTitleVote(user.getId(),keyword,pageable);
        }

        List<Information> list = page.getContent();

        model.addAttribute("user",user);
        model.addAttribute("order", order);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("informationList", list);
//        model.addAttribute("pageIndex", pageIndex);
//        model.addAttribute("pageSize", pageSize);
        return (async==true?"/information/u :: #mainContainerRepleace":"/information/u");
    }

    /**
     * 企业管理自己的资讯发布
     * @param order
     * @param categoryId
     * @param keyword
     * @param async
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/companyInformation")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY')")  // 指定角色权限才能操作方法
    public String companyInformation(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                         @RequestParam(value = "category",required = false,defaultValue = "0") Long categoryId,
                                         @RequestParam(value = "keyword", required = false,defaultValue = "")String keyword,
                                         @RequestParam(value = "async",required = false) boolean async,
                                         @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                         Model model){
        //获取当前登录的用户
        User user = UserUtils.getUser();

        Page<Information> page = null;

        if(categoryId != null && categoryId>0){
            InformationCategory informationCategory = informationCategoryService.getInformationCategoryById(categoryId);
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = informationService.listInformationByCategory(user.getId(),informationCategory,pageable);
        }else if (order.equals("hot")){
            Sort sort = new Sort(Sort.Direction.DESC,"readSize");
            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = informationService.listInformationByTitleVoteAndSort(user.getId(),keyword,pageable);
        }else if (order.equals("new")){
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = informationService.listInformationByTitleVote(user.getId(),keyword,pageable);
        }

        List<Information> list = page.getContent();

        model.addAttribute("user",user);
        model.addAttribute("order", order);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("informationList", list);
//        model.addAttribute("pageIndex", pageIndex);
//        model.addAttribute("pageSize", pageSize);
        return (async==true?"/information/companyInformation :: #mainContainerRepleace":"/information/companyInformation");
    }

    /**
     * 保存资讯(增加或者修改资讯)
     * @param information
     * @return
     */
    @PostMapping("/{username}/edit")
    //@PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveOrUpdateInformation(@PathVariable("username") String username, @RequestBody Information information) {
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
                originalInformation.setInformationCategory(information.getInformationCategory());
                originalInformation.setAuthor(information.getAuthor());
                informationService.saveInformation(originalInformation);
            } else {
                information.setUser(userService.getUserIdByUsername(username));
                informationService.saveInformation(information);
            }

        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/information/" +username + "/" +information.getId();
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }

    /**
     * 保存资讯(增加或者修改资讯)
     * @param information
     * @return
     */
    @PostMapping("/companyEdit")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY')")  // 指定角色权限才能操作方法
    //@PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> companyEdit(@RequestBody Information information) {
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
                originalInformation.setInformationCategory(information.getInformationCategory());
                originalInformation.setAuthor(information.getAuthor());
                informationService.saveInformation(originalInformation);
            } else {
                information.setUser(UserUtils.getUser().getId());
                informationService.saveInformation(information);
            }

        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/information/companyInformation";
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }


    /**
     * 获取新增资讯页面
     * @param model
     * @return
     */
    @GetMapping("/{username}/create")
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView createInformation(Model model,
                                          @PathVariable(value = "username") String username){
        List<InformationCategory> categories = informationCategoryService.listInformationCategory();       //获取所有资讯分类

        model.addAttribute("information",new Information(null,null,null));          //创建一个新的资讯对象
        model.addAttribute("categories",categories);
        model.addAttribute("username", username);
        return new ModelAndView("/information/informationedit","informationModel",model);
    }

    /**
     * 企业用户获取新增资讯页面
     * @param model
     * @return
     */
    @GetMapping("/companyCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY')")  // 指定角色权限才能操作方法
    public String companyCreate(Model model) {
        model.addAttribute("information",new Information(null,null,null));          //创建一个新的资讯对象
        return "/information/companyInformationEdit";
    }

    /**
     * 获取修改资讯页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/companyInformationEdit")
    @PreAuthorize("hasAnyAuthority('ROLE_COMPANY')")  // 指定角色权限才能操作方法
    public String companyInformationEdit(@RequestParam("id") Long id, Model model){
        Information information = informationService.getInformationById(id);
        if (information == null || !information.getUser().equals(UserUtils.getUser().getId())) {
            return "/login";
        }
        model.addAttribute("information",information);            //根据id获取资讯对象
        return "/information/companyInformationEdit";
    }
    /**
     * 获取修改资讯页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/edit/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView editInformation(@PathVariable("username") String username,@PathVariable("id") Long id, Model model){

        Information information = informationService.getInformationById(id);
        if (information.getUser() != UserUtils.getUser().getId()) {
            return new ModelAndView("/login","informationModel",model);
        }
        List<InformationCategory> categories = informationCategoryService.listInformationCategory();
        information.getContent();
        model.addAttribute("information",informationService.getInformationById(id));            //根据id获取资讯对象
        model.addAttribute("categories",categories);
        return new ModelAndView("/information/informationedit","informationModel",model);
    }
    /**
     * 获得资讯展示页面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/{id}")
    public String getInformationById(@PathVariable("username") String username, @PathVariable("id") Long id,Model model){
        Information information = informationService.getInformationById(id);

        User author = userService.findUserById(information.getUser());

        //每次读取，简单地看作阅读量加1
        informationService.readingIncrease(id);

        //判断用户是否是资讯的所有者
        boolean isInformationOwner = false;
        boolean isCompanyStyle = false;
        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (user !=null && user.getId().equals(information.getUser())) {
                isInformationOwner = true;
            }
        }
        if (isInformationOwner == true && author.getStyle().equals(UserContants.STYLE_COMAPNY)) {
            isInformationOwner = false;
            isCompanyStyle = true;
        }
        model.addAttribute("isInformationOwner",isInformationOwner);
        model.addAttribute("isCompanyStyle",isCompanyStyle);
        model.addAttribute("informationModel",information);
        model.addAttribute("user", author);

        return "/information/informationShow";
    }

    /**
     * 根据id删除资讯
     * @param username
     * @param id
     * @return
     */
    @DeleteMapping("/{username}/{id}")
    //@PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteInformation(@PathVariable("username") String username,@PathVariable("id") Long id) {

        try {
            informationService.removeInformation(id);
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/information/" + username ;
        return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
    }


    ///////////////////////////////////
    //以下为管理员端
    ///////////////////////////////////

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public ResultVO deleteInformation(@RequestParam("id") Long id) {
        try {
            informationService.removeInformation(id);
        } catch (Exception e) {
            return ResultUtils.error(1, "删除失败");
        }
        return ResultUtils.success();
    }

    /**
     * 编辑资讯内容
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/ShowInformationAdmins")
    public String informationUpdate(@RequestParam("id") Long id,
                                    Model model) {
        Information information = informationService.getInformationById(id);
        model.addAttribute("information", information);
        return "admins/pages/news/news_detail";
    }

    /**
     * 用户统计页面
     * @return
     */
    @GetMapping("/informationStatisticsCharts")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String toUserStatisticsCharts(Model model) {
        return "admins/pages/charts/information_statistics_charts";
    }

    /**
     * 按分类统计数据
     * @return
     */
    @RequestMapping("/informationCountByStyle")
    @ResponseBody
    public Map<String,Object> informationCountByStyle() {
        Map<String,Object> map = new HashMap<>();
        List<ChartsValueCountVo> list = informationService.informationCountByStyle();
        List<String> nameList = list.stream().map(ChartsValueCountVo -> ChartsValueCountVo.getName()).collect(Collectors.toList());
        map.put("nameList", nameList);
        map.put("list", list);
        map.put("text","创新创业平台资讯分类扇形图");
        map.put("subtext", "选择时间内分类资讯总量");
        map.put("unit","资讯数量");
        return map;
    }

    /**
     * 按热度统计数据
     * @return
     */
    @RequestMapping("/informationCountByHot")
    @ResponseBody
    public Map<String,Object> informationCountByHot() {
        Map<String,Object> map = new HashMap<>();
        Pageable pageable = new PageRequest(0,10);
        List<ChartsValueCountVo> list = informationService.informationCountByHot(pageable);
        List<String> nameList = list.stream().map(ChartsValueCountVo -> ChartsValueCountVo.getName()).collect(Collectors.toList());
        map.put("nameList", nameList);
        map.put("list", list);
        map.put("text","创新创业平台资讯热度TOP10扇形图");
        map.put("subtext", "选择时间内资讯热度");
        map.put("unit","资讯热度");
        return map;
    }
}
