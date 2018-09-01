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
import com.cxcy.zjb.springboot.Vo.ProductionVo;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.utils.CheckWordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;


/**
 * 作品控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/production")
@EnableScheduling       //开启定时任务
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

    @Value("${video.VideoPath}")
    private String VideoPath;

    /**
     * 定时审核作品:凌晨1点
     */
    @Scheduled(cron = "* * 1 * * ?")
    public void checkWord() {
        //1.获取所有的未审核作品：0：未审核
        List<Production> list = productionService.findByPCheck(0);
        //2.遍历所有作品，对作品进行文字过滤
        for (Production production:list) {
            String fileName = production.getPContent();
            ResultVO resultVO = checkWord(fileName);
            if(resultVO.getCode()==0){
                production.setPCheck(1);
            }else{
                production.setPCheck(2);
            }
            productionService.save(production);
        }
    }


    //    跳转到编辑页面
    @RequestMapping(value = "/toUploadProduction")
    public  ModelAndView toUploadProduction(String username,Long pId,Map map) {
        map.put("userName",username);
        map.put("pId",pId);
        return new ModelAndView("production/uploadProduction",map);
    }

    /**
     * 进行全文搜索
     * @param keyword
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @GetMapping("/listAll")
    public @ResponseBody ResultVO listEsProductions(
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

    /**
     * 用户查询自己所有的作品，包括未审核和审核不通过的
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/center/user{userId}")
    public ModelAndView centerShowProduction(@PathVariable("userId") Long userId,
                                              @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                              @RequestParam(value = "size", required = false, defaultValue = "3") Integer pageSize,
                                              Map map){

        //设置分页
        Pageable pageable = new PageRequest(pageIndex-1, pageSize);
        Page<Production> productions = productionService.findAllByUserId(userId,pageable);
        if(productions.getTotalPages()==0){
            map.put("productionPage",null);
        }else{
            map.put("productionPage",productions);
        }
        String url = "/production/center/user"+userId;
        User user = userService.findUserById(userId);
        map.put("url",url);
        map.put("userName",user.getUsername());
        map.put("page",pageIndex);
        map.put("size",pageSize);
        return new ModelAndView("production/productionCenter",map);
    }

    // 分页显示用户的所有作品信息
    @GetMapping("/{username}/productionCenter")
    /*@PreAuthorize("authentication.name.equals(#username)")*///先不添加，自己判断
    public ModelAndView showAllProduction(@PathVariable("username") String username ,
                                                    @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                                    @RequestParam(value = "size", required = false, defaultValue = "3") Integer pageSize,
                                                    Map map){
        //设置分页
        Pageable pageable = new PageRequest(pageIndex-1, pageSize);
//        根据用户名查找用户
        Page<Production> productions;
        User user = userService.findByUsername(username);
        Long uId = user.getId();
//      根据用户ID查找所有的作品
        productions = productionService.findByUserAndPCheck(uId,pageable);
        System.out.println(uId);
        System.out.println(productions.getTotalPages());
        if(productions.getTotalPages()==0){
            map.put("productionPage",null);
        }else{
            map.put("productionPage",productions);
        }
        String url = "/production/"+username+"/productionCenter";
        map.put("user",username);
        map.put("url",url);
        map.put("page",pageIndex);
        map.put("size",pageSize);
        return new ModelAndView("production/productionOtherCenter",map);
    }

    //    删除对应的作品:需要先判断作品是否作者的，好像不用判断，当显示作品详情的时 候已经对作品的作者进行判断了才会显示删除接口
    @GetMapping("/{username}/deleteProduction/{pId}")
    /*@PreAuthorize("authentication.name.equals(#username)")*///先不添加，自己判断
    public @ResponseBody ResultVO deleteProduction(@PathVariable("username") String username,@PathVariable("pId") Long pId) {
        boolean isProductionOwner = false;
        Production production = productionService.findByPId(pId);
        Long uId = production.getUser();
        User user = userService.findUserById(uId);
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
            //String redirectUrl = username + "/producition";
            return ResultUtils.success();
        }
        else{
            return ResultUtils.error(2,"无权删除");
        }
    }

    /**
     * 查看作品
     * @param request
     * @param pId
     * @param model
     * @return
     */
    @RequestMapping(value="/{pId}")
    public /*@ResponseBody ResultVO*/ModelAndView getProductionByPId(HttpServletRequest request, @PathVariable("pId") Long pId,Model model) {
        User user = (User) request.getSession().getAttribute("user");
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
        Long id = user.getId();
        if(uId.equals(id)){
            isProductionOwner = true;
        }

//        根据pid查找对应的作品
//        Production production = productionService.findByPId(pId);
        List list = new ArrayList();

        // 判断操作用户的点赞情况
        List<Vote> votes = production.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况

        for (Vote vote : votes) {
            if(vote.getUser().equals(id)) {
                currentVote = vote;
                break;
            }
        }

        //根据production的分类id查找出对应的内容和方向内容
        Long cId = production.getCatagorys();
        Catagorys catagory = catagoryService.findByCatagorysId(cId);
        Long dId = catagory.getDirection();
        Direction direction = directionService.findById(dId);

        //修改用户的最后浏览记录
        Browse browse = browseService.findCatagoryByUserId(id);
        if(browse==null){
            browse = new Browse();
            browse.setUser(id);
        }
        browse.setCatagory(cId);
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        browse.setBrowseTime(dateString);
        browseService.saveLastBrowse(browse);


        model.addAttribute("direction",direction);
        model.addAttribute("catagory",catagory);
        model.addAttribute("currentVote",currentVote);
        model.addAttribute("isProductionOwner",isProductionOwner);
        list.add(model);
        list.add(production);
        return new ModelAndView("/production/showProduction", "productionModel", list);
//        return ResultUtils.success(list);
    }
    /**
     * 作品分类展示,通过地址获取类别id，通过id查找出此id的作品
     * @param catagory
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/nav/cata{catagory}")
    public ModelAndView displayPro( @PathVariable("catagory") Long catagory,
                                     @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "size", required = false, defaultValue = "3") Integer pageSize,
                                    Map map){
        List<Production> productions = productionService.findProductionsByCategoryId(catagory);
        String url = "/production/nav/cata"+catagory;
        map.put("url",url);
        map.put("catagory","cata"+catagory);
        System.out.println(productions);
        if(productions.size()==0){//找不到作品的情况
            map.put("productionPage", null);
        }else {
            Catagorys catagorys = catagoryService.findByCatagorysId(catagory);//得到类别的对象
            String caName =catagorys.getCaName();//得到类别的名字
            String direction = directionService.findById(catagorys.getDirection()).getDName();//得到对应的方向

            List<ProductionVo> productionVoList = new ArrayList<>();//将查到的数据封装到list中
            for(Production production : productions){
                ProductionVo productionVo = new ProductionVo();

                productionVo.setDirection(direction);
                productionVo.setCatagorys(caName);
                productionVo.setUserName(userService.findUserById(production.getUser()).getUsername());
                productionVo.setProduction(production);

                productionVoList.add(productionVo);
            }
            List<ProductionVo> list;
            if(pageIndex * pageSize>productionVoList.size()){
               list =  productionVoList.subList((pageIndex - 1) * pageSize, productionVoList.size());
            }else {
                list = productionVoList.subList((pageIndex - 1) * pageSize, (pageIndex * pageSize));
            }
            map.put("productionPage", list);
            int totalSize = (int)((productionVoList.size()+pageSize-1)/pageSize);

            map.put("totalSize",totalSize);
        }


        map.put("page",pageIndex);
        map.put("size",pageSize);
        return new ModelAndView("production/amateurPro",map);
    }

    /**
     * 最新最热推荐
     * @param nh
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/nav/{nh}")
    public ModelAndView displayProASNewHot(@PathVariable("nh") String nh,
                                                   @RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                                   @RequestParam(value = "size", required = false, defaultValue = "3") Integer pageSize,
                                                   Map map){
        String url = "/production/nav/"+nh;
        map.put("catagory",nh);
        map.put("url",url);
        List<Production> productions = null;
        if (nh.equals("hot")) { // 最热查询
            Sort sort = new Sort(DESC,"eVoteSize","commentSize","readSize");
            productions = productionService.findAll(sort);
        }
        if (nh.equals("new")) { // 最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            productions = productionService.findTop10orderByTimeDesc();
        }

        List<ProductionVo> productionVoList = new ArrayList<>();//将查到的数据封装到list中
        for(Production production : productions){
            ProductionVo productionVo = new ProductionVo();
            //按类别id查出类别的名称和方向id，由方向id查出方向的名称
            Catagorys catagorys = catagoryService.findByCatagorysId(production.getCatagorys());
            productionVo.setDirection(directionService.findById(catagorys.getDirection()).getDName());
            productionVo.setCatagorys(catagorys.getCaName());
            //查出作者的昵称
            productionVo.setUserName(userService.findUserById(production.getUser()).getUsername());
            //加入作品信息
            productionVo.setProduction(production);
            //存储在list里面
            productionVoList.add(productionVo);
        }
        List<ProductionVo> list;
        if(pageIndex * pageSize>productionVoList.size()){
            list =  productionVoList.subList((pageIndex - 1) * pageSize, productionVoList.size());
        }else {
            list = productionVoList.subList((pageIndex - 1) * pageSize, (pageIndex * pageSize));
        }
        map.put("productionPage", list);

        int totalSize = (int)((productionVoList.size()+pageSize-1)/pageSize);
        map.put("totalSize",totalSize);

        map.put("page",pageIndex);
        map.put("size",pageSize);
        return new ModelAndView("production/amateurPro",map);
    }

    /**
     * 转存视频文件
     * @param file
     * @param url
     * @return
     */
    public String saveVideo(MultipartFile file,String url){
        String video = null;
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.indexOf(".")+1);
            if(!suffix.equals("mp4")){
                return null;
            }
            try {
                String path = VideoPath.substring(VideoPath.indexOf("/")+1);
                String filePath = path + url +"/"+ file.getOriginalFilename();
                System.out.println(filePath);
                video=url+"/"+file.getOriginalFilename();
                // 转存文件
                file.transferTo(new File(filePath));
                System.out.println(video);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return video;
    }

    /**
     * 文档敏感词审核
     * @param fileName
     * @return
     */
    @GetMapping("/checkWord")
    public @ResponseBody ResultVO checkWord(String fileName){
        String suffix = fileName.substring(fileName.indexOf(".")+1);
        CheckWordUtil checkWordUtil = new CheckWordUtil();
        String msg = "";
        try {
            if("doc".equals(suffix)){
                msg = checkWordUtil.readFileAsDoc(fileName);
            }else if("docx".equals(suffix)){
                msg = checkWordUtil.readFileAsDocx(fileName);
            }else{
                msg = "请上传doc、docx的文件";
            }
        }catch(Exception e){
            e.printStackTrace();
            msg = "文件格式有误，请重新上传";
        }
       ResultVO resultVO;
        if("文件审核通过".equals(msg)){
            resultVO = ResultUtils.success(msg);
        }else{
            resultVO = ResultUtils.error(1,msg);
        }
        return resultVO;
    }

    /**
     * 转存文件
     * @param file
     * @return
     */
    public String saveFile(MultipartFile file){
        String filePath = "";
        String parentFile = "E:\\Workspace\\Temp\\file\\";
        filePath =  file.getOriginalFilename();
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
        // 判断扩展名为doc或者docx
        filePath = parentFile + newFileName + suffix;
        File in = new File(filePath);
        File dest = in.getParentFile();
        filePath = newFileName + suffix;
        if (!dest.exists()) //如果这个文件不存在
        {
            dest.mkdirs(); //创建
        }
        try {
            file.transferTo(in); // copy
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }


    /**
     * 新建或者修改作品，成功则返回对应的作品路径
     * @param request
     * @param pId
     * @param pTitle
     * @param pSummary
     * @param pSort
     * @param Catagorys
     * @param pContent
     * @param videoFile
     * @return
     */
    @RequestMapping(value="/saveProduction")
    public @ResponseBody ResultVO saveProduction(HttpServletRequest request,
                            @RequestParam(value="pId",required = false) Long pId,@RequestParam("pTitle") String pTitle,
                            @RequestParam(value="pSummary") String pSummary,
                            @RequestParam("pSort") Integer pSort, @RequestParam(value ="Catagorys") Long Catagorys,
                            @RequestParam("pContent") MultipartFile pContent, @RequestParam(value="videoFile",required = false) MultipartFile videoFile) {
        User user = (User) request.getSession().getAttribute("user");
        try {
            Production production;
            // 判断是修改还是新增
            if(pId!=null){  //说明是修改
//                根据已有ID查找对应的作品
                Production orignalProduction = productionService.findByPId(pId);
//                前端允许修改的是作品的内容，视频路径，作品的类别，作品的标题,作品的简介
                orignalProduction.setPTitle(pTitle);
                orignalProduction.setPSort(pSort);
//                orignalProduction.setPVideo(pVideo);
                orignalProduction.setPSummary(pSummary);
                production = orignalProduction;
            }else {
                // 查找到当前的用户
                Long uid = user.getId();
                Production newproduction = new Production();//新建一个作品
                newproduction.setUser(uid);
                newproduction.setPTitle(pTitle);
                newproduction.setPSort(pSort);
                newproduction.setCatagorys(Catagorys);
                newproduction.setPSummary(pSummary);
                production = newproduction;
            }
            //保存文件
            String filePath = saveFile(pContent);
            //保存视频
            if(videoFile!=null){
                String video = saveVideo(videoFile,"video");
                if(video!=null){
                    production.setPVideo(video);
                }
            }
            production.setPContent(filePath);
            Production newPro = productionService.save(production);
            pId = newPro.getPId();//获取新的作品的id
        } catch (Exception e) {
            e.printStackTrace();
        }
        String redirectUrl = "/production/" + pId;
        return ResultUtils.success(redirectUrl);
    }

    /**
     * 取消或者点赞
     * @param request
     * @param pId
     * @return
     */
    @GetMapping("/addOrRemoveVote/{pId}")
    public @ResponseBody ResultVO addOrRemoveVote(HttpServletRequest request,@PathVariable("pId") Long pId) {
       User user = (User)request.getSession().getAttribute("user");
        try{
           productionService.createVoteOrRemoveVote(pId,user);
       }catch (Exception e){
           return ResultUtils.error(1,"操作错误");
       }
        return ResultUtils.success();
    }


    //获取当前的点赞数和评论数
    @GetMapping("/countVoteAndComment/{pId}")
    public @ResponseBody ResultVO countVoteAndComment(@PathVariable("pId") Long pId,Model model) {
        Production production = productionService.findByPId(pId);
        Integer voteSize = production.getEVoteSize();
        Integer commentSize = production.getCommentSize();
        model.addAttribute("voteSize",voteSize);
        model.addAttribute("commentSize",commentSize);
        return ResultUtils.success(model);
    }

}









