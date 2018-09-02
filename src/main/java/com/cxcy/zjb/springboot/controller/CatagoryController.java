package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.CatagoryVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

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
    public ModelAndView findCatagorysByDid(@RequestParam("direction") long direction, Map map){
        List<Catagorys> list = catagoryService.findByDid(direction);
        List<CatagoryVo> vos = new ArrayList<>();
        for(Catagorys catagorys:list){
            int size = productionService.findByCid(catagorys.getCId()).size();
            CatagoryVo catagoryVo = new CatagoryVo(catagorys,size);
            vos.add(catagoryVo);
        }

        map.put("vos",vos);
        return new ModelAndView("/admins/pages/manage/production/product_classify_detail",map);
    }

    /**
     * 删除分类
     * @param cId
     * @return
     */
    @GetMapping("/deleteCatagorysById")
    public @ResponseBody
    ResultVO deleteCatagorysById(@RequestParam("cId") long cId){
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
     * 修改或增加分类
     * @param cId
     * @param dId
     * @param caName
     * @return
     */
    @GetMapping("/saveCatagorysById")
    public @ResponseBody
    ResultVO saveCatagorysById(@RequestParam(value="cId",required=false) Long cId,
                               @RequestParam(value="dId") Long dId,
                                @RequestParam(value="caName") String caName){
        Catagorys catagorys;
        if(cId != null){
            catagorys = catagoryService.findById(cId);
            catagorys.setCaName(caName);
        }else {
            catagorys = new Catagorys(dId,caName);
        }
        Catagorys newcata = catagoryService.save(catagorys);
        if(newcata != null){
            return ResultUtils.success(catagorys);
        }else{
            return ResultUtils.error(1,"添加不成功");
        }
    }
}
