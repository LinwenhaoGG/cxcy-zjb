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
import org.springframework.web.bind.annotation.*;

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
        List<Catagorys> catagorys = catagoryService.findByDid(dId);
        if (catagorys.size()!=0) {
            return ResultUtils.error(1,"此方向下有分类，无法删除");
        }else{
            directionService.deleteDir(dId);
            return ResultUtils.success();
        }

    }
    //    跳转到管理员端方向、分类管理页面
    @RequestMapping(value = "/proClassify")
    public String proClassify() {
        return "/admins/pages/manage/production/production_classify";
    }
    /**
     * 修改方向
     * @param dId
     * @param dName
     * @return
     */
    @GetMapping("/updateDirection")
    public @ResponseBody
    ResultVO updateDirection(@RequestParam(value="dId") Long dId,
                             @RequestParam(value="dName") String dName) {
        Direction direction = directionService.findById(dId);
        direction.setDName(dName);
        Direction byName = directionService.findByName(dName);
        if(byName != null){
            return ResultUtils.error(1, "方向已存在");
        }
        Direction save = directionService.save(direction);
        if (save != null) {
            return ResultUtils.success(save);
        } else {
            return ResultUtils.error(1, "添加不成功");
        }
    }
    /**
     * 添加方向和多个分类
     * @param dName
     * @param caNames
     * @return
     */
    @PostMapping("/saveDirAndCata")
    public @ResponseBody
    ResultVO saveDirAndCata(@RequestParam(value="dName") String dName,
                           @RequestParam(value="caName[]") String[] caNames){
        ResultVO resultVO = ResultUtils.error(2,"添加方向成功，分类名称重复，添加失败"); ;
        Direction direction = directionService.findByName(dName);
        if(direction != null){
            resultVO = ResultUtils.error(1,"已含有此方向");
            return resultVO;
        }
        direction = new Direction(dName);
        directionService.save(direction);
        for(int i = 0;i<caNames.length;i++){
            Catagorys cata = catagoryService.findByCatagorysByName(caNames[i]);
            if(cata == null){
                Catagorys catagorys = new Catagorys(direction.getDId(),caNames[i]);
                catagoryService.save(catagorys);
                resultVO = ResultUtils.success();
            }
        }
        return resultVO;
    }
}
