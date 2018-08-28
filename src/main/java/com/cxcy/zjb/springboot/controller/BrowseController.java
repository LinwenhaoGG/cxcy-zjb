package com.cxcy.zjb.springboot.controller;


import com.cxcy.zjb.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 个性化推荐控制层
 */
@Controller
@RequestMapping("/browse")
public class BrowseController {

    @Autowired
    private BrowseService browseService;
    @Autowired
    private ProductionService productionService;
    @Autowired
    private CatagoryService catagoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private DirectionService directionService;


}
