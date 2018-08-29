package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ProductionVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.CheckWordUtil;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 作品控制层
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/production")
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

    //跳转到上传视频的页面
    @GetMapping("/cxcyHtml")
    public ModelAndView toUpFile(Model model){

        return new ModelAndView("amateurPro", "videa", model);
    }
    //得到上传的视频，并转存
    @PostMapping("/uploadVideo")
    public @ResponseBody Object uploadVideo(HttpServletRequest request,@RequestParam("videoFile") MultipartFile videoFile){
        ResultVO resultVO = new ResultVO();
        String video = saveVideo(request,videoFile,"video");
        resultVO.setData(video);
        return resultVO;
    }

    /**
     * 作品分类展示,通过地址获取类别id，通过id查找出此id的作品
     * 只是显示作品列表，因此没有查点赞的人和评论的人
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
        return new ModelAndView("amateurPro",map);
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
        return new ModelAndView("amateurPro",map);
    }

    /**
     * 转存视频文件
     * @param request
     * @param file
     * @param url
     * @return
     */
    public String saveVideo(HttpServletRequest request,MultipartFile file,String url){
        String video="123.mp4";
        if (!file.isEmpty()) {
            try {
                String path = VideoPath.substring(VideoPath.indexOf("/")+1);
                String filePath = path + url +"/"+ file.getOriginalFilename();
                System.out.println(filePath);
                video="temp/"+url+"/"+file.getOriginalFilename();
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
    public @ResponseBody Object checkWord(String fileName){
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

}
