package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import com.cxcy.zjb.springboot.Vo.ProductionVo;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.CheckWordUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 作品控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
public class ProductionController {
    @Value("${video.VideoPath}")
    private String VideoPath;

    @Autowired
    private ProductionService productionService;
    @Autowired
    private CatagoryService catagoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private DirectionService directionService;

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
            Sort sort = new Sort(Sort.Direction.DESC,"eVoteSize","commentSize","readSize");
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
     * 新建或者修改作品，成功则返回对应的作品路径
     * @param username
     * @param pId
     * @param pTitle
     * @param pSummary
     * @param pSort
     * @param Catagorys
     * @param pContent
     * @param videoFile
     * @return
     */
    @RequestMapping(value="/{username}/saveProduction",method=RequestMethod.POST)
    public @ResponseBody
    ResultVO saveProduction(@PathVariable("username") String username,
                            @RequestParam(value="pId",required = false) Long pId,@RequestParam("pTitle") String pTitle,
                            @RequestParam(value="pSummary") String pSummary,
                            @RequestParam("pSort") Integer pSort, @RequestParam(value ="Catagorys") Long Catagorys,
                            @RequestParam("pContent") MultipartFile pContent, @RequestParam(value="videoFile",required = false) MultipartFile videoFile) {
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
                // 根据用户名查找到当前的用户
                User user = userService.findByUsername(username);
                Long uid = user.getId();
                Production newproduction = new Production();//新建一个作品
                newproduction.setUser(uid);
                newproduction.setPTitle(pTitle);
                newproduction.setPSort(pSort);
                newproduction.setCatagorys(Catagorys);

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
            if(videoFile!=null){
                String video = saveVideo(videoFile,"video");
                if(video==null){
                    return ResultUtils.error(2,"视频格式错误");
                }
                production.setPVideo(video);
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









