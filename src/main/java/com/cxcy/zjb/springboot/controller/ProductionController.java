package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 作品控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class ProductionController {
    @Autowired
    private ProductionService productionService;
    @Autowired
    private UserService userService;

    @Autowired
    private CatagoryService catagoryService;

    @Autowired
    private DirectionService directionService;

    //    跳转测试页面
    @RequestMapping(value = "/totest")
    public String totest() {
        return "file/index";
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
                            @RequestParam("pSort") Integer pSort, @RequestParam(value ="Catagorys") Long Catagorys,
                            @RequestParam("pContent") MultipartFile pContent, @RequestParam(value = "pVideo", required = false) String pVideo) {
        try {
            Production production;
            // 判断是修改还是新增
            if(pId!=null){  //说明是修改
//                根据已有ID查找对应的作品
                Production orignalProduction = productionService.findByPId(pId);
//                前端允许修改的是作品的内容，视频路径，作品的类别，作品的标题
                orignalProduction.setPTitle(pTitle);
                orignalProduction.setPSort(pSort);
                orignalProduction.setPVideo(pVideo);
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









