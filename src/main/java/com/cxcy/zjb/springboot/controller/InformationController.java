package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.InformationVo;
import com.cxcy.zjb.springboot.Vo.Response;
import com.cxcy.zjb.springboot.domain.Information;

import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.InformationCategoryService;
import com.cxcy.zjb.springboot.service.InformationService;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/")
    public String informationIndex(){
        return "/information/index";
    }

    @GetMapping("/showTop8")
//    @ResponseBody
    public String getTop8Notice(@RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "8") int pageSize,
                                Model model){
        Long categoryId = 2L;
        InformationCategory informationCategory = informationCategoryService.getInformationCategoryById(categoryId);
        Page<Information> page = null;
        Pageable pageable = new PageRequest(pageIndex,pageSize);
        page = informationService.listInformationOrderByCreateTimeDesc(informationCategory,pageable);
        List<Information> list = page.getContent();
//        List<InformationVo> volist = new ArrayList<InformationVo>();
//        for (Information information : list
//             ) {
//            InformationVo informationVo = new InformationVo();
//            BeanUtils.copyProperties(information,informationVo);
//            volist.add(informationVo);
//
//        }
        model.addAttribute("informationList",list);
        return "index :: #informationShowByCategory";

    }

    @GetMapping("/showOneInformation/{id}")
    public String showOneInformation(@PathVariable("id") Long id,Model model){
        Information information = informationService.getInformationById(id);
        informationService.readingIncrease(id);
        model.addAttribute("informationModel",information);
        return "/information/showOne";
    }

    @GetMapping("/showAllNotice")
    public String showAllNotice(@RequestParam(value = "pageIndex",required = false,defaultValue = "1") int pageIndex,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                Model model){
        Long categoryId = 2L;
        InformationCategory informationCategory = informationCategoryService.getInformationCategoryById(categoryId);
        Page<Information> page = null;
        Pageable pageable = new PageRequest(pageIndex-1,pageSize);
        page = informationService.listInformationOrderByCreateTimeDesc(informationCategory,pageable);
        List<Information> list = page.getContent();
        model.addAttribute("informationList",list);
        model.addAttribute("page",page);
        model.addAttribute("pageIndex",pageIndex);
        model.addAttribute("pageSize",pageSize);

        return "/information/showAllNotice :: #mainContainerRepleace";
    }

    @GetMapping("/{username}")
    public String listInformationByOrder(@PathVariable("username") String username,
                                         @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                         @RequestParam(value = "category",required = false) Long categoryId,
                                         @RequestParam(value = "keyword", required = false,defaultValue = "")String keyword,
                                         @RequestParam(value = "async",required = false) boolean async,
                                         @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                         @RequestParam(value = "pageSize",required = false,defaultValue = "5") int pageSize,
                                         Model model){
        User user = (User) userDetailsService.loadUserByUsername(username);

        Page<Information> page = null;

        if(categoryId != null && categoryId>0){
            InformationCategory informationCategory = informationCategoryService.getInformationCategoryById(categoryId);
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = informationService.listInformationByCategory(informationCategory,pageable);
        }else if (order.equals("hot")){
            Sort sort = new Sort(Sort.Direction.DESC,"readSize");
            Pageable pageable = new PageRequest(pageIndex,pageSize,sort);
            page = informationService.listInformationByTitleVoteAndSort(user,keyword,pageable);
        }else if (order.equals("new")){
            Pageable pageable = new PageRequest(pageIndex,pageSize);
            page = informationService.listInformationByTitleVote(user,keyword,pageable);
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
                originalInformation.setInformationCategory(information.getInformationCategory());
                originalInformation.setAuthor(information.getAuthor());
                informationService.saveInformation(originalInformation);
            } else {
                User user = (User)userDetailsService.loadUserByUsername(username);
                information.setUser(user);
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
     * 获取新增资讯页面
     * @param model
     * @return
     */
    @GetMapping("/{username}/create")
    public ModelAndView createInformation(Model model){
        List<InformationCategory> categories = informationCategoryService.listInformationCategory();       //获取所有资讯分类

        model.addAttribute("information",new Information(null,null,null));          //创建一个新的资讯对象
        model.addAttribute("categories",categories);
        return new ModelAndView("/information/informationedit","informationModel",model);
    }

    /**
     * 获取修改资讯页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/edit/{id}")
    public ModelAndView editInformation(@PathVariable("username") String username,@PathVariable("id") Long id, Model model){
        List<InformationCategory> categories = informationCategoryService.listInformationCategory();

        Information information = informationService.getInformationById(id);
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
        User principal = null;
        Information information = informationService.getInformationById(id);

        //每次读取，简单地看作阅读量加1
        informationService.readingIncrease(id);

        //判断用户是否是资讯的所有者
        boolean isInformationOwner = false;
//        if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
//                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
//            principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            if (principal !=null && username.equals(principal.getUsername())) {
//                isInformationOwner = true;
//            }
//        }

        if(username.equals(information.getUser().getUsername())){
            isInformationOwner = true;
        }
        model.addAttribute("isInformationOwner",isInformationOwner);
        model.addAttribute("informationModel",information);

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


}
