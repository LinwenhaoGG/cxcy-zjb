package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
        PageRequest request = new PageRequest(page - 1, size,sort);
        //根据排序查出分页
        Page<Matchs> matchsPage = matchService.findAll(request);
        map.put("matchsPage", matchsPage);
        map.put("page", page);
        map.put("size", size);

        return new ModelAndView("matchs/adminsMatchsList", map);
    }
}
