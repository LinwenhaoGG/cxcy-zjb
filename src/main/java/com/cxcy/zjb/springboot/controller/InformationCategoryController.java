package com.cxcy.zjb.springboot.controller;

import com.cxcy.zjb.springboot.Vo.Response;
import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.service.InformationCategoryService;
import com.cxcy.zjb.springboot.utils.ConstraintViolationExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id){
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
    @PostMapping
    public ResponseEntity<Response> addInformationCategory(@RequestBody InformationCategory informationCategory){

        try {
            informationCategoryService.saveInformationCategory(informationCategory);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "处理成功", null));
    }

    /**
     * 获取分类编辑界面
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String getCatalogEdit(Model model) {
        InformationCategory catalog = new InformationCategory(null, null);
        model.addAttribute("catalog",catalog);
        return "index2";
    }


    /**
     * 根据 Id 获取分类信息
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id") Long id, Model model) {
        InformationCategory catalog = informationCategoryService.getInformationCategoryById(id);
        model.addAttribute("catalog",catalog);
        return "";
    }
}
