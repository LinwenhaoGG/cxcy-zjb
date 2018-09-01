/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CatagoryService
 * Author:   KOLO
 * Date:     2018/8/20 10:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Catagorys;

import java.util.List;

import com.cxcy.zjb.springboot.domain.Catagorys;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface CatagoryService {

    List<Catagorys> findByDid(Long direction);




    /**
     * 通过类别id查找
     * @param catagorysId
     * @return
     */
    public Catagorys findByCatagorysId(Long catagorysId);

    /**
     * 查找所有分类
     * @return
     */
    public List<Catagorys> findAll();

    /**
     * 删除分类
     * @param Cid
     */
    public void deleteCataByCid(Long Cid);

    /**
     * 通过主键查找分类
     * @param cId
     * @return
     */
    public Catagorys findById(Long cId);

    /**
     * 添加或修改分类
     * @param catagorys
     * @return
     */
    public Catagorys save(Catagorys catagorys);
}
