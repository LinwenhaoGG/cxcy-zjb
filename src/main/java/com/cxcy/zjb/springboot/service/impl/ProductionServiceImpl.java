/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductionServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.repository.ProductionRepository;
import com.cxcy.zjb.springboot.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    private ProductionRepository productionRepository;

    @Override
    public List<Production> findProductionsByCategoryId(Long categoryId) {
       List<Production> productions =productionRepository.findByCatagorysAndPCheck(categoryId,0);
       // Page<Production> list = productionRepository.findByPCheck(0);
        return productions;
    }

    @Override
    public List<Production> findAll(Sort sort) {
        return productionRepository.findTop10ByPCheck(0,sort);
    }

    @Override
    public List<Production> findTop10orderByTimeDesc() {
        return productionRepository.findTop10ByPCheckOrderByPuploadTimeDesc(0);
    }


    @Override
    public List<Production> findOrderByTimeDesc() {
        return productionRepository.findByPCheckOrderByPuploadTimeDesc(0);
    }

    @Override
    public List<Production> findFirst7ByCatagorysAndPCheck(Long catagoryId, Sort sort) {
        return productionRepository.findFirst7ByCatagorysAndPCheck(catagoryId,0,sort);
    }
}