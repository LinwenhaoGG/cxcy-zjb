package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.domain.es.EsProduction;
import com.cxcy.zjb.springboot.service.*;
import com.cxcy.zjb.springboot.service.es.EsProductionService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.data.domain.Sort.Direction.DESC;

/**
 * 作品控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class ProductionController {

    @Autowired
    private EsProductionService esProductionService;
    @Autowired
    private ProductionService productionService;
    @Autowired
    private UserService userService;

    @Autowired
    private CatagoryService catagoryService;

    @Autowired
    private DirectionService directionService;

    @Autowired
    private BrowseService browseService;

    //    跳转测试页面
    @RequestMapping(value = "/totest")
    public String totest() {
        return "file/index";
    }

    /**
     * 进行全文搜索，首次点击主页面的作品页面时，默认首次加载，其他情况下点击进入是async必须为true
     * @param order
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("listAll")
    public @ResponseBody ResultVO listEsProductions(
            @RequestParam(value="order",required=false,defaultValue="hot") String order,//查询时默认按最热
            @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,//关键字默认为“”，全部搜索
            @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,//默认从第一页开始搜索
            @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,//默认每一页有10行数据
            Model model) {

        Page<EsProduction> page = null;
        List<EsProduction> list = null;
//        boolean isEmpty = true; // 系统初始化时，没有作品数据
        try {
            Sort sort = new Sort(DESC,"readSize","voteSize","commentSize","createTime");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = esProductionService.listHotestEsProductions(keyword, pageable);
//            isEmpty = false;
        } catch (Exception e) {
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = esProductionService.listEsProductions(pageable);
        }

        list = page.getContent();	// 当前所在页面数据列表


        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("productionList", list);



        return ResultUtils.success(model);
    }






    //      显示用户的所有作品信息
    @GetMapping("/{username}/production")
    /*@PreAuthorize("authentication.name.equals(#username)")*///先不添加，自己判断
    public @ResponseBody ResultVO showAllProduction(@PathVariable("username") String username) {
//        根据用户名查找用户
        List<Production> productions = new ArrayList<>();
        try {
            User user = userService.findByUsername(username);
            Long uId = user.getId();
//        根据用户ID查找所有的作品
            productions = productionService.findByUser(uId);
        }catch(Exception e) {
            return ResultUtils.error(1, "显示异常");
        }
        return ResultUtils.success(productions);
    }

//    删除对应的作品:需要先判断作品是否作者的，好像不用判断，当显示作品详情的时 候已经对作品的作者进行判断了才会显示删除接口
    @GetMapping("/{username}/deleteProduction/{pId}")
    /*@PreAuthorize("authentication.name.equals(#username)")*///先不添加，自己判断
    public @ResponseBody ResultVO deleteProduction(@PathVariable("username") String username,@PathVariable("pId") Long pId) {
        boolean isProductionOwner = false;
        // 判断操作用户是否是作品的所有者
        /*if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())) {
                isProductionOwner = true;//作品是作者的，可以显示编辑删除等功能
            }
        }*/
        Production production = productionService.findByPId(pId);
        Long uId = production.getUser();
        User user = userService.findByUId(uId);
        String username1 = user.getUsername();
        if(username.equals(username1)){
            isProductionOwner = true;
        }
        if(isProductionOwner) {
            try {
                productionService.deleteByPId(pId);
            } catch (Exception e) {
                return ResultUtils.error(1, "删除失败");
            }
            //      删除成功会跳转到用户的个人作品显示页面
            String redirectUrl = username + "/producition";
            return ResultUtils.success(redirectUrl);
        }
        else{
            return ResultUtils.error(2,"无权删除");
        }
    }

    /**
     * 查看作品
     * @param username
     * @param pId
     * @return
     */
    @GetMapping("/{username}/production/{pId}")
    public @ResponseBody ResultVO getProductionByPId(@PathVariable("username") String username,@PathVariable("pId") Long pId,Model model) {
        // 每次读取，简单的可以认为阅读量增加1次
        productionService.readingIncrease(pId);



//      查看作品的是否作者本身，初始化否
        boolean isProductionOwner = false;
        // 判断操作用户是否是作品的所有者
        /*if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal !=null && username.equals(principal.getUsername())) {
                isProductionOwner = true;//作品是作者的，可以显示编辑删除等功能
            }
        }*/

        Production production = productionService.findByPId(pId);
        Long uId = production.getUser();
        User user = userService.findByUId(uId);
        String username1 = user.getUsername();
        if(username.equals(username1)){
            isProductionOwner = true;
        }

//        根据pid查找对应的作品
//        Production production = productionService.findByPId(pId);
        List list = new ArrayList();

        // 判断操作用户的点赞情况
        List<Vote> votes = production.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况

        for (Vote vote : votes) {
            if(vote.getUser().equals(userService.findByUsername(username).getId())) {
                currentVote = vote;
                break;
            }
        }

        //根据production的分类id查找出对应的内容和方向内容
        Long cId = production.getCatagorys();
        Catagorys catagory = catagoryService.findOne(cId);
        Long dId = catagory.getDirection();
        Direction direction = directionService.findByID(dId);


        //修改用户的最后浏览记录
        Browse browse = browseService.findCatagoryByUserId(uId);
        if(browse==null){
            browse = new Browse();
            browse.setUser(uId);
        }
        browse.setCatagory(cId);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        browse.setBrowseTime(dateString);
        browseService.saveLastBrowse(browse);

        //设置这个分类浏览数量加1
        catagoryService.readingIncrease(cId);

        model.addAttribute("direction",direction);
        model.addAttribute("catagory",catagory);

        model.addAttribute("currentVote",currentVote);
        model.addAttribute("isProductionOwner",isProductionOwner);
        list.add(model);
        list.add(production);
        return ResultUtils.success(list);
    }

    /**
     * 新建或者修改作品，成功则返回对应的作品路径
     * @param username
     * @param pId
     * @param pTitle
     * @param pSort
     * @param Catagorys
     * @param pContent
     * @param pVideo
     * @return
     */
    @RequestMapping(value="/{username}/saveProduction",method=RequestMethod.POST)
    public @ResponseBody
    ResultVO saveProduction(@PathVariable("username") String username,
                            @RequestParam(value="pId",required = false) Long pId,@RequestParam("pTitle") String pTitle,
                            @RequestParam(value="pSummary") String pSummary,
                            @RequestParam("pSort") Integer pSort, @RequestParam(value ="Catagorys") Long Catagorys,
                            @RequestParam("pContent") MultipartFile pContent, @RequestParam(value = "pVideo", required = false) String pVideo) {
        try {
            Production production;
            // 判断是修改还是新增
            if(pId!=null){  //说明是修改
//                根据已有ID查找对应的作品
                Production orignalProduction = productionService.findByPId(pId);
//                前端允许修改的是作品的内容，视频路径，作品的类别，作品的标题,作品的简介
                orignalProduction.setPTitle(pTitle);
                orignalProduction.setPSort(pSort);
                orignalProduction.setPVideo(pVideo);
                orignalProduction.setPSummary(pSummary);
                production = orignalProduction;
            }else {
                // 根据用户名查找到当前的用户
                User user = userService.findByUsername(username);
                Long uid = user.getId();
                Production newproduction = new Production();//新建一个作品
                newproduction.setUser(uid);
                newproduction.setPTitle(pTitle);
                newproduction.setPSort(pSort);
                newproduction.setCatagorys(Catagorys);
                newproduction.setPVideo(pVideo);
                newproduction.setPSummary(pSummary);
                production = newproduction;

            }
            String filePath = "";
            String parentFile = "E:\\Workspace\\Temp\\file\\";
            filePath =  pContent.getOriginalFilename();
            //文件名称在服务器有可能重复
            String newFileName="";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            newFileName = sdf.format(new Date());
            Random r = new Random();
            for(int i =0 ;i<3;i++){
                newFileName=newFileName+r.nextInt(10);
            }
            //获取文件扩展名
            String suffix = filePath.substring(filePath.lastIndexOf("."));
//            判断扩展名为doc或者docx
            if(suffix.equals(".doc")||suffix.equals(".docx")) {
                filePath = parentFile + newFileName + suffix;
                File in = new File(filePath);
                File dest = in.getParentFile();
                filePath = newFileName + suffix;
                if (!dest.exists()) //如果这个文件不存在
                {
                    dest.mkdirs(); //创建
                }
                pContent.transferTo(in); // copy
            }
            else{
                return ResultUtils.error(2,"文件上传格式不符合要求，请重新上传");
            }
            production.setPContent(filePath);
            productionService.save(production);
            pId = production.getPId();//获取新的作品的id
        } catch (Exception e) {
            return ResultUtils.error(1,"添加不成功");
        }
       String redirectUrl = "/" + username + "/production/" + pId;
        return ResultUtils.success(redirectUrl);
    }

    /**
     * 取消或者点赞
     * @param username
     * @param pId
     * @return
     */
    @GetMapping("/{username}/addOrRemoveVote/{pId}")
    public @ResponseBody ResultVO getProductionByPId(@PathVariable("username") String username,@PathVariable("pId") Long pId) {
       try{
           productionService.createVoteOrRemoveVote(pId,username);
       }catch (Exception e){
           return ResultUtils.error(1,"操作错误");
       }
        return ResultUtils.success();
    }
}









