/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DirectionService
 * Author:   KOLO
 * Date:     2018/8/20 10:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Direction;

import java.util.List;


/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface DirectionService {

    List<Direction> findAll();


    /**
     * 通过方向id查找
     * @param directionId
     * @return
     */
    public Direction findById(Long directionId);

    /**
     * 通过名称查找
     * @param name
     * @return
     */
    public Direction findByName(String name);

    /**
     * 删除方向
     * @param dId
     */
    public void deleteDir(Long dId);

    /**
     * 添加方向
     * @param direction
     * @return
     */
    public Direction save(Direction direction);
}
