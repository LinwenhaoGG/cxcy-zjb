/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: GrowthServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:37
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Growth;
import com.cxcy.zjb.springboot.repository.GrowthRepository;
import com.cxcy.zjb.springboot.service.GrowthService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GrowthServiceImpl implements GrowthService {
    @Autowired
    public GrowthRepository growthRepository;


    @Override
    public Growth saveGrowth(Growth growth) {
        return growthRepository.save(growth);
    }

    /**
     * 根据用户的id查找到对应的成长对象
     * @param uId
     * @return
     */
    @Override
    public Growth findByUser(Long uId) {
        return growthRepository.findByUser(uId);
    }

    /**
     * 保存或者更新相应的成长对象
     * @param growth
     * @return
     */
    @Override
    public Growth save(Growth growth) {
        return growthRepository.save(growth);
    }

    /**
     * 查找所有的成长记录
     * @return
     */
    @Override
    public List<Growth> findAll() {
        return growthRepository.findAll();
    }

    @Override
    public List<Growth> findAll(Sort sort) {
        return growthRepository.findAll(sort);
    }


}