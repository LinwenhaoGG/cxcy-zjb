package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.ProductionVo;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.domain.Direction;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.enums.ResultEnum;
import com.cxcy.zjb.springboot.service.CatagoryService;
import com.cxcy.zjb.springboot.service.DirectionService;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.utils.CheckWordUtil;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    //跳转到上传视频的页面
    @GetMapping("/toUpFile")
    public ModelAndView toUpFile(Model model){

        return new ModelAndView("production/uploadVideo", "videa", model);
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
    public @ResponseBody Object displayPro(@PathVariable("catagory") Long catagory,Integer pageIndex,Integer pageSize){
        //设置分页
        Pageable pageable = new PageRequest(pageIndex, pageSize);
        Page<Production> productions = productionService.findProductionsByCategoryId(catagory,pageable);
        ResultVO resultVO;//存放返回的数据
        if(productions == null){//找不到作品的情况
            resultVO = ResultUtils.error(ResultEnum.NO_PRODUCTION.getCode(),ResultEnum.NO_PRODUCTION.getMessage());
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
            resultVO = ResultUtils.success(productionVoList);
        }
        return resultVO;
    }

    @GetMapping("/nav/{nh}")
    public @ResponseBody Object displayProASNewHot(@PathVariable("nh") String nh,Integer pageIndex,Integer pageSize){
        ResultVO<Production> resultVO = null;
        Page<Production> productions = null;
        if (nh.equals("hot")) { // 最热查询
            Sort sort = new Sort(Sort.Direction.DESC,"eVoteSize","commentSize","readSize");

            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            productions = productionService.findAll(pageable);
        }
        if (nh.equals("new")) { // 最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            productions = productionService.findOrderByTimeDesc(pageable);
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

            productionVo.setProduction(production);

            productionVoList.add(productionVo);
        }
        resultVO = ResultUtils.success(productionVoList);
        return resultVO;
    }
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
