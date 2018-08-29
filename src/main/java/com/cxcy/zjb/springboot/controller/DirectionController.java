package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Direction;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 方向控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/direction")
public class DirectionController {

    @Autowired
    private DirectionService directionService;

    @GetMapping("/findAllDirection")
    public @ResponseBody
    ResultVO findAllDirection(Integer pageIndex,Integer pageSize){
        //设置分页
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<Direction> list = directionService.findAll(pageable);
        return ResultUtils.success(list);
    }
}
