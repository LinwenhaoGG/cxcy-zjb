package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.DirectionVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Direction;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**f
     * 查找所有的方向和底下分类的数量
     * @return
     */
    @GetMapping("/findAllDirectionAndNum")
    public @ResponseBody
    ResultVO findAllDirectionAndNum(){
        List<Direction> list = directionService.findAll();
        List<DirectionVo> directionVos = new ArrayList<>();
       for (Direction direction:list){
           List<Catagorys> catagorys = catagoryService.findByDid(direction.getDId());
           DirectionVo vo = new DirectionVo(direction,catagorys.size());
           directionVos.add(vo);
       }

        return ResultUtils.success(directionVos);
    }

    /**
     * 删除方向
     * @param dId
     * @return
     */
    @GetMapping("/deleteDirection")
    public @ResponseBody
    ResultVO deleteDirection(@RequestParam(value="dId") Long dId){
        Catagorys catagorys = catagoryService.findByCatagorysId(dId);
        if (catagorys != null) {
            return ResultUtils.error(1,"此方向下有分类，无法删除");
        }else{
            directionService.deleteDir(dId);
            return ResultUtils.success();
        }

    }
    //    跳转测试页面
    @RequestMapping(value = "/totest")
    public String totest() {
      /*  User user = userService.findByUsername("zpr");
//        User user = userService.findUserById(1224L);
        session.setAttribute("user",user);
//      return "/production/showProduction";
//        return user;*/
        return "/admins/pages/manage/production/production_classify";
    }
}
