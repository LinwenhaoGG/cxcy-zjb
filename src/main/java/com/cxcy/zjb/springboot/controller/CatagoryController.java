package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.CatagoryVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import javafx.scene.effect.SepiaTone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 分类控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/catagory")
public class CatagoryController {
    @Autowired
    private CatagoryService catagoryService;
    @Autowired
    private ProductionService productionService;

    /**
     * 查出所有的分类
     * @return
     */
    @GetMapping("/allCatagorys")
    public @ResponseBody Object findAllCatagorys(){
        List<Catagorys> catagorys = catagoryService.findAll();
        ResultVO resultVO = ResultUtils.success(catagorys);
        return resultVO;
    }

    /**
     * 通过方向查找分类和分类下作品的数量
     * @param direction
     * @return
     */
    @GetMapping("/findCatagorysByDid")
    public ModelAndView findCatagorysByDid(@RequestParam("direction") Long direction, Map map){
        List<Catagorys> list = catagoryService.findByDid(direction);
        List<CatagoryVo> vos = new ArrayList<>();
        for(Catagorys catagorys:list){
            int size = productionService.findByCid(catagorys.getCId()).size();
            CatagoryVo catagoryVo = new CatagoryVo(catagorys,size);
            vos.add(catagoryVo);
        }
        System.out.println(vos.toString());
        map.put("vos",vos);
        map.put("dId",direction);
        return new ModelAndView("/admins/pages/manage/production/product_classify_detail",map);
    }

    /**
     * 删除分类
     * @param cId
     * @return
     */
    @GetMapping("/deleteCatagorysById/{cId}")
    public @ResponseBody
    ResultVO deleteCatagorysById(@PathVariable("cId") Long cId){
        //如果分类下有作品，则无法删除，若无，则可以删除
        List<Production> productions = productionService.findByCid(cId);
        if(productions.size()!=0){
            return ResultUtils.error(1,"分类下有作品，无法删除");
        }else{
            catagoryService.deleteCataByCid(cId);
            return ResultUtils.success();
        }

    }

    /**
     * 修改分类
     * @param cId
     * @param dId
     * @param caName
     * @return
     */
    @GetMapping("/saveCatagorysById")
    public @ResponseBody
    ResultVO saveCatagorysById(@RequestParam(value="cId",required=false) Long cId,
                                @RequestParam(value="caName") String caName){
        Catagorys catagorys;
            catagorys = catagoryService.findById(cId);
            catagorys.setCaName(caName);
        Catagorys newcata = catagoryService.save(catagorys);
        if(newcata.getCaName().equals(caName)){
            return ResultUtils.success(catagorys);
        }else{
            return ResultUtils.error(1,"修改不成功");
        }
    }

    /**
     * 添加多个分类
     * @param dId
     * @param caNames
     * @return
     */
    @PostMapping("/saveCatagorys")
    public @ResponseBody
    ResultVO saveCatagorys(@RequestParam(value="dId") Long dId,
                           @RequestParam(value="caName[]") String[] caNames){
        ResultVO resultVO = ResultUtils.error(1,"已有分类，添加失败");
        for(int i = 0;i<caNames.length;i++){
            Catagorys cata = catagoryService.findByCatagorysByName(caNames[i]);
            if(cata == null){
                Catagorys catagorys = new Catagorys(dId,caNames[i]);
                Catagorys newcata = catagoryService.save(catagorys);
                resultVO = ResultUtils.success();
            }
        }
        return resultVO;
    }
}
