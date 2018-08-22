package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 分类控制曾
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class CatagoryController {

    @Autowired
    private CatagoryService catagoryService;

    @GetMapping("/findCatagorysByDid")
    public @ResponseBody
    ResultVO findCatagorysByDid(@RequestParam("direction") long direction){
        List<Catagorys> list = catagoryService.findByDid(direction);
        return ResultUtils.success(list);
    }
}
