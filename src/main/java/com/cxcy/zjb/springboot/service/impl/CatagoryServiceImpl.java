/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CatagoryServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:32
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Catagorys;
import com.cxcy.zjb.springboot.repository.CatagoryRepository;
import com.cxcy.zjb.springboot.service.CatagoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CatagoryServiceImpl implements CatagoryService {

    @Autowired
    private CatagoryRepository catagoryRepository;


    @Override
    public List<Catagorys> findByDid(Long direction) {
        return catagoryRepository.findByDirection(direction);
    }

    @Override
    public Catagorys findByCatagorysId(Long catagorysId) {
        Catagorys catagorys = catagoryRepository.findOne(catagorysId);
        return catagorys;
    }

    @Override
    public Catagorys findByCatagorysByName(String name) {
        return catagoryRepository.findByCaName(name);
    }

    @Override
    public List<Catagorys> findAll() {
        return catagoryRepository.findAll();
    }

    @Override
    public void deleteCataByCid(Long Cid) {
        catagoryRepository.delete(Cid);
    }

    @Override
    public Catagorys findById(Long cId) {
        return catagoryRepository.findOne(cId);
    }

    @Override
    public Catagorys save(Catagorys catagorys) {

        return catagoryRepository.save(catagorys);
    }


}