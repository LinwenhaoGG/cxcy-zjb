package com.cxcy.zjb.springboot.controller;


import com.cxcy.zjb.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxcy.zjb.springboot.Vo.ProductionVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    /**
     * 个性化推荐（推荐10条作品），最多7条按分类推荐优秀作品，再按时间顺序取最新发表的作品，凑到10条
     * @param request
     * @return
     */
    @GetMapping("/recommend")
    public @ResponseBody
    Object recommend(HttpServletRequest request){
        //Long userId = (Long)request.getSession().getAttribute("user");
        Long userId = 1L;//模拟数据，具体应该是从session取出登录的id
        Long catagory = browseService.findCatagoryByUserId(userId).getCatagory();//查询到该用户最后一条浏览记录的分类
        //按点赞数、评论量、阅读量排序
        Sort sort = new Sort(Sort.Direction.DESC,"eVoteSize","commentSize","readSize");
        //通过分类查询7条作品记录
        List<Production> productions = productionService.findFirst7ByCatagorysAndPCheck(catagory,sort);
        Set<Production> set = new HashSet<>();//将查到的数据存储在set中，再去加上最新推荐，凑到10条
        for (Production production : productions){
            System.out.println(production);
            set.add(production);
        }
        List<Production> orderByTimeDesc = productionService.findOrderByTimeDesc();
        for (int i = 0;i<orderByTimeDesc.size() && set.size()<10;i++){//共10条数据或数据库没有10条数据时停止
            System.out.println(orderByTimeDesc.get(i));
            set.add(orderByTimeDesc.get(i));
        }
        System.out.println(set.size());
        List<ProductionVo> productionVoList = new ArrayList<>();//将查到的数据封装到list中
        for(Production p : set){
            ProductionVo productionVo = new ProductionVo();
            //按类别id查出类别的名称和方向id，由方向id查出方向的名称
            Catagorys catagorys = catagoryService.findByCatagorysId(p.getCatagorys());
            productionVo.setDirection(directionService.findById(catagorys.getDirection()).getDName());
            productionVo.setCatagorys(catagorys.getCaName());
            //查出作者的昵称
            productionVo.setUserName(userService.findUserById(p.getUser()).getUsername());

            productionVo.setProduction(p);

            productionVoList.add(productionVo);
        }

        //将查到的数据加在json格式中
        ResultVO resultVO = ResultUtils.success(productionVoList);
        return resultVO;

    }
}
