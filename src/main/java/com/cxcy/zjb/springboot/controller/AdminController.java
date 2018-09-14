package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Response;
import com.cxcy.zjb.springboot.domain.Information;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.InformationCategoryService;
import com.cxcy.zjb.springboot.service.InformationService;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
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
    private MatchService matchService;

    @Autowired
    private InformationCategoryService informationCategoryService;

    @Autowired
    private InformationService informationService;

    @Autowired
    private UserService userService;

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
}
