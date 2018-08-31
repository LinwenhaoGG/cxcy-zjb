/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: InformationCategoryService
 * Author:   KOLO
 * Date:     2018/8/20 10:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.InformationCategory;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface InformationCategoryService {

    /**
     * 获取InformationCategory列表
     * @return
     */
    List<InformationCategory> listInformationCategory();

    /**
     * 根据id删除分类
     * @param id
     */
    void removeInformationCategory(Long id);

    /**
     * 添加/保存分类
     * @param informationCategory
     * @return
     */
    InformationCategory saveInformationCategory(InformationCategory informationCategory);

    /**
     * 根据id获取分类
     * @param id
     * @return
     */
    public InformationCategory getInformationCategoryById(Long id);
}
