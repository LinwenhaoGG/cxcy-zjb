package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.MatchService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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


    /**
     * 保存比赛
     * @param
     * @return
     */
    @RequestMapping
    @ResponseBody
    public ResultVO saveMatchs(Matchs matchs) {
        log.info(matchs.toString());
        Matchs re; //数据库保存后返回的值
        if (matchs.getId() != null) {//判断是增加还是修改
            //如果是修改
            Matchs orignalMatchs = matchService.getMatchById(matchs.getId());
            orignalMatchs.setName(matchs.getName());
            orignalMatchs.setContent(matchs.getContent());
            orignalMatchs.setHtmlContent(matchs.getHtmlContent());
            re=matchService.saveMatch(orignalMatchs);
        } else {
            re=matchService.saveMatch(matchs);
        }
       return ResultUtils.success(re.getId());
    }

    /**
     * 新增编辑页面
     * @param map
     * @return
     */
    @RequestMapping("/edit")
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
        return new ModelAndView("matchs/edit", map);
    }

    /**
     * 编辑
     * @param id
     * @param map
     * @return
     */
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable(value = "id") long id, Map map) {

        Matchs matchs = matchService.getMatchById(id);
        //通过类型查找用户列表，2为教师
        List<User> userList = userService.findUserListByStyle(2);

        map.put("userList",userList);
        map.put("matchs", matchs);
        return new ModelAndView("matchs/edit", map);
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
        return new ModelAndView("matchs/view", map);
    }

    /**
     * 全部比赛信息预览
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView listMatchs(Map map) {
        List<Matchs> matchsList = matchService.findAll();
        map.put("matchsList", matchsList);
        return new ModelAndView("matchs/matchsList", map);
    }

    /**
     * 全部比赛信息预览
     * @param map
     * @return
     */
    @GetMapping("/adminslist")
    public ModelAndView listMatchsAdmin(Map map) {
        List<Matchs> matchsList = matchService.findAll();
        map.put("matchsList", matchsList);
        return new ModelAndView("matchs/adminsMatchsList", map);
    }

    /**
     * 删除
     * @param id 要删除的比赛id
     * @return
     */
    @GetMapping("/delete/{id}")
    public ModelAndView deletematchs(@PathVariable(value = "id") long id) {

        matchService.deleteMatchById(id);

        return new ModelAndView("matchs/success");
    }

}
