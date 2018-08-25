package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Direction;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 方向控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class DirectionController {

    @Autowired
    private DirectionService directionService;

    @GetMapping("/findAllDirection")
    public @ResponseBody
    ResultVO findAllDirection(){
        List<Direction> list = directionService.findAll();
        return ResultUtils.success(list);
    }
}
