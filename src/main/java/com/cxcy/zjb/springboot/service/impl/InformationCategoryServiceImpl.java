/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InformationCategoryServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.InformationCategory;
import com.cxcy.zjb.springboot.repository.InformationCategoryRepository;
import com.cxcy.zjb.springboot.service.InformationCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
@Service
public class InformationCategoryServiceImpl implements InformationCategoryService {

    @Autowired
    private InformationCategoryRepository informationCategoryRepository;

    /**
     * 查找出分类列表
     * @return
     */
    @Override
    public List<InformationCategory> listInformationCategory() {
        List<InformationCategory> informationCategories = informationCategoryRepository.findAll();
        return informationCategories;
    }

    /**
     * 根据id删除列表
     * @param id
     */
    @Override
    public void removeInformationCategory(Long id) {
        informationCategoryRepository.delete(id);
    }

    /**
     * 保存分类
     * @param informationCategory
     * @return
     */
    @Override
    public InformationCategory saveInformationCategory(InformationCategory informationCategory) {
        //判断分类是否重复
        List<InformationCategory> list = informationCategoryRepository.findByName(informationCategory.getName());
        if ( list != null && list.size()>0 ){
            throw new IllegalArgumentException("该分类已经存在！");
        }
        return informationCategoryRepository.save(informationCategory);
    }

    /**
     * 根据id查找分类
     * @param id
     * @return
     */
    @Override
    public InformationCategory getInformationCategoryById(Long id) {
        return informationCategoryRepository.findOne(id);
    }

    @Override
    public Page<InformationCategory> findAllByPage(Pageable pageable) {
        return informationCategoryRepository.findAll(pageable);
    }
}