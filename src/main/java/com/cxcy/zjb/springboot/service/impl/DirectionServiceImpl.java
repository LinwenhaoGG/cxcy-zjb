/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DirectionServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Direction;
import com.cxcy.zjb.springboot.repository.DirectionRepository;
import com.cxcy.zjb.springboot.service.DirectionService;
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
public class DirectionServiceImpl implements DirectionService {

    @Autowired
    private DirectionRepository directionRepository;

    @Override
    public List<Direction> findAll() {
        return directionRepository.findAll();
    }
}