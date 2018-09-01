package com.cxcy.zjb.springboot.controller;


import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxcy.zjb.springboot.Vo.ProductionVo;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    public @ResponseBody Object recommend(HttpServletRequest request,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                          @RequestParam(value = "size", required = false, defaultValue = "3") Integer pageSize,
                                          Map map) {
        String url = "/browse/recommend";
        map.put("url", url);
        map.put("catagory", "recommend");
        User user = (User)request.getSession().getAttribute("user");
        Set<Production> set = new HashSet<>();//将查到的数据存储在set中，再去加上最新推荐，凑到10条
        if(user != null) {
            Long userId = user.getId();
            Long catagory = browseService.findCatagoryByUserId(userId).getCatagory();//查询到该用户最后一条浏览记录的分类
            //按点赞数、评论量、阅读量排序
            Sort sort = new Sort(Sort.Direction.DESC, "eVoteSize", "commentSize", "readSize");
            //通过分类查询7条作品记录
            List<Production> productions = productionService.findFirst7ByCatagorysAndPCheck(catagory, sort);

            for (Production production : productions) {
                System.out.println(production);
                set.add(production);
            }
        }
        List<Production> orderByTimeDesc = productionService.findOrderByTimeDesc();
        for (int i = 0; i < orderByTimeDesc.size() && set.size() < 10; i++) {//共10条数据或数据库没有10条数据时停止
            System.out.println(orderByTimeDesc.get(i));
            set.add(orderByTimeDesc.get(i));
        }
        System.out.println(set.size());
        List<ProductionVo> productionVoList = new ArrayList<>();//将查到的数据封装到list中
        for (Production p : set) {
            ProductionVo productionVo = new ProductionVo();
            //按类别id查出类别的名称和方向id，由方向id查出方向的名称
            Catagorys catagorys = catagoryService.findByCatagorysId(p.getCatagorys());
            productionVo.setCatagorys(catagorys.getCaName());
            productionVo.setDirection(directionService.findById(catagorys.getDirection()).getDName());
            //查出作者的昵称
            productionVo.setUserName(userService.findUserById(p.getUser()).getUsername());

            productionVo.setProduction(p);

            productionVoList.add(productionVo);
        }
        List<ProductionVo> list;
        if (pageIndex * pageSize > productionVoList.size()) {
            list = productionVoList.subList((pageIndex - 1) * pageSize, productionVoList.size());
        } else {
            list = productionVoList.subList((pageIndex - 1) * pageSize, (pageIndex * pageSize));
        }
        map.put("productionPage", list);
        int totalSize = (int) ((productionVoList.size() + pageSize - 1) / pageSize);

        map.put("totalSize", totalSize);

        map.put("page", pageIndex);
        map.put("size", pageSize);
        return new ModelAndView("production/amateurPro", map);
    }
}
