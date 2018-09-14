package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Response;
import com.cxcy.zjb.springboot.Vo.ResultVO;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.service.InformationCategoryService;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
import com.cxcy.zjb.springboot.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 资讯分类控制器
 * Created by LINWENHAO on 2018/8/20.
 */
@Controller
@RequestMapping("/infCategory")
public class InformationCategoryController {

    @Autowired
    private InformationCategoryService informationCategoryService;

    /**
     * 查出所有的分类
     * @return
     */
    @GetMapping("/allCatagorys")
    public @ResponseBody Object findAllCatagorys(){
        List<InformationCategory> catagorys = informationCategoryService.listInformationCategory();
        ResultVO resultVO = ResultUtils.success(catagorys);
        return resultVO;
    }

    /**
     * 获取资讯分类列表
     * @param model
     * @return
     */
    @GetMapping
    //@ResponseBody
    public String listInformationCategory(Model model) {
        List<InformationCategory> informationCategories = informationCategoryService.listInformationCategory();

        model.addAttribute("informationCategories", informationCategories);


        return "/information/u :: #categoryRepleace";
    }


    /**
     * 根据id删除分类
     * @return
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public ResponseEntity<Response> delete(@RequestParam("id") Long id){
        try {
            informationCategoryService.removeInformationCategory(id);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 添加/保存分类
     * @param informationCategory
     */
    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String addInformationCategory(InformationCategory informationCategory){

        try {
            informationCategoryService.saveInformationCategory(informationCategory);
        } catch (Exception e)  {
            return "redirect:/admins/toInformationCategorieList";
        }

        return "redirect:/admins/toInformationCategorieList";
    }

    /**
     * 获取分类编辑界面
     * @param model
     * @return
     */
    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
    public String getCatalogupdate(Model model) {
        InformationCategory catalog = new InformationCategory(null, null);
        model.addAttribute("catalog",catalog);
        return "admins/pages/news/news_classify_alter";
    }


    /**
     * 根据 Id 获取分类信息
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/update")
    public String getCatalogById(@RequestParam("id") Long id, Model model) {
        InformationCategory catalog = informationCategoryService.getInformationCategoryById(id);
        model.addAttribute("catalog",catalog);
        return "admins/pages/news/news_classify_alter";
    }
}
