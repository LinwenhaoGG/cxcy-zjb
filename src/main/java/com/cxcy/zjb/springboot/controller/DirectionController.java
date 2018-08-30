package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Direction;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private CatagoryService catagoryService;

    /**f
     * 查找所有的方向和第一个方向对应的分类
     * @return
     */
    @GetMapping("/findAllDirection")
    public @ResponseBody
    ResultVO findAllDirection(Model model){
        List<Direction> list = directionService.findAll();
        Direction direction = list.get(0);
        List<Catagorys> list1 = catagoryService.findByDid(direction.getDId());
        model.addAttribute("list",list);
        model.addAttribute("list1",list1);
        return ResultUtils.success(model);
    }
}
