package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.converter.EventMatchGroup2EventAchievement;
import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.MatchGroup;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.dto.EventAchievement;
import com.cxcy.zjb.springboot.service.MatchGroupService;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 比赛控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/match")
@Slf4j
public class MatchController {

    @Autowired
    private UserService userService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchGroupService matchGroupService;

    /**
     * 保存比赛
     * @param
     * @return
     */
    @RequestMapping
    @ResponseBody
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ResultVO saveMatchs(@RequestBody Matchs matchs) {
        log.info(matchs.toString());
        Matchs re; //数据库保存后返回的值
        //获取当前登录的账号
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long uid = loginUser.getId();//发布人id
        if (matchs.getId() != null) {//判断是增加还是修改
            //如果是修改
            Matchs orignalMatchs = matchService.getMatchById(matchs.getId());
            orignalMatchs.setName(matchs.getName());
            orignalMatchs.setContent(matchs.getContent());
            orignalMatchs.setHtmlContent(matchs.getHtmlContent());
            re=matchService.saveMatch(orignalMatchs);
        } else {
            matchs.setUser(uid);
            re=matchService.saveMatch(matchs);
        }
       return ResultUtils.success(re.getId());
    }

    /**
     * 新增编辑页面
     * @param map
     * @return
     */
    @GetMapping("/edit")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView addMatchs(Map map) {
        Matchs matchs = new Matchs();
        matchs.setOverTime(new Date());
        matchs.setStartTime(new Date());
        matchs.setLastsubmittime(new Date());
        matchs.setLastsigntime(new Date());
        //通过类型查找用户列表，2为教师
        List<User> userList = userService.findUserListByStyle(2);

        map.put("userList",userList);
        map.put("matchs",matchs);
        return new ModelAndView("matchs/matchsEdit", map);
    }

    /**
     * 编辑
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView edit(@PathVariable(value = "id") long id, Map map) {

        Matchs matchs = matchService.getMatchById(id);
        //通过类型查找用户列表，2为教师
        List<User> userList = userService.findUserListByStyle(2);

        map.put("userList",userList);
        map.put("matchs", matchs);
        return new ModelAndView("matchs/matchsEdit", map);
    }

    /**
     * 预览
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/preview/{id}")
    public ModelAndView preview(@PathVariable(value = "id") Long id, Map map) {

        Matchs matchs = matchService.getMatchById(id);


        map.put("matchs", matchs);
        return new ModelAndView("matchs/matchsView", map);
    }

    /**
     * 查看全部比赛信息
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView listMatchs(@RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                   @RequestParam(value = "size", defaultValue = "6") Integer size,//一页个数
                                   Map map) {
        //根据开始时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"StartTime");
        PageRequest request = new PageRequest(page - 1, size, sort);
        //根据排序查出分页
        Page<Matchs> matchsPage = matchService.findAll(request);
        map.put("matchsPage", matchsPage);
        map.put("page", page);
        map.put("size", size);
        if (matchsPage.getTotalPages() == 0) {
            map.put("isnull", 1);
        } else {
            map.remove("isnull");
        }
        return new ModelAndView("matchs/matchsList", map);
    }

    /**
     * 老师查看查看自己发布的比赛信息
     * @param map
     * @return
     */
    @GetMapping("/teacherList")
    @PreAuthorize("hasAnyAuthority('ROLE_TEACHER')")  // 指定角色权限才能操作方法
    public ModelAndView listMatchsTeacher(@RequestParam(value = "page", defaultValue = "1") Integer page, //页数
                                   @RequestParam(value = "size", defaultValue = "5") Integer size,//一页个数
                                   Map map) {
        //获取当前登录的老师账号
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取当前老师id
        Long uid = loginUser.getId();
        //根据开始时间排序
        Sort sort = new Sort(org.springframework.data.domain.Sort.Direction.DESC,"StartTime");
        PageRequest request = new PageRequest(page - 1, size);
        //根据排序查出分页
        Page<Matchs> matchsPage = matchService.findAllByUser(uid,request);
        map.put("matchsPage", matchsPage);
        map.put("page", page);
        map.put("size", size);
        if (matchsPage.getTotalPages() == 0) {
            map.put("isnull", 1);
        }else {
            map.remove("isnull");
        }

        return new ModelAndView("matchs/teacher/teacherMatchsList", map);
    }



    /**
     * 删除
     * @param mid 要删除的比赛id
     * @return
     */
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    @ResponseBody
    public ResultVO deletematchs(@RequestParam("mid") Long mid) {
        matchService.deleteMatchById(mid);
        return ResultUtils.success();
    }

    /**
     * 查看比赛成绩
     * @return
     */
    @GetMapping("/matchAchievement/{mid}")
    public ModelAndView machsAchievement(@PathVariable(value = "mid") Long mid, Model model) {
        //获取该比赛的成绩
        Matchs matchs = matchService.getMatchById(mid);
        List<List<EventAchievement>> eventAchievementList = new ArrayList<>();

        for (Event event : matchs.getEventList()) {
            List<MatchGroup> matchGroups = matchGroupService.getMatchGroupByEventOrderByAchievement(event.getId());
            List<EventAchievement> achievementList = EventMatchGroup2EventAchievement.conver(matchGroups, event);
            eventAchievementList.add(achievementList);
        }

        model.addAttribute("eventAchievementList",eventAchievementList);
        model.addAttribute("matchsName",matchs.getName());
        return new ModelAndView("matchs/matchsAchievement","model",model);
    }
}
