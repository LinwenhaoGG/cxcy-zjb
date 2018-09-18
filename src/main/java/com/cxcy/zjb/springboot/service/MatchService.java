/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MatchService
 * Author:   KOLO
 * Date:     2018/8/20 10:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.Matchs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface MatchService {
    /*
    保存比赛
     */
    Matchs saveMatch(Matchs matchs);

    /*
    通过id查找
     */
    Matchs getMatchById(Long id);

    /*
    查询所有比赛
     */
    Page<Matchs> findAll(Pageable pageable);

    /**
     * 删除比赛
     */
    void deleteMatchById(Long id);

    /**
     * 添加项目
     */
    Matchs createEvent(Long matchsId, Event event);

    /**
     * 删除项目
     */
    void deleteEvent(Long matchsId, Long eventId);

    /*
    查询老师编写的比赛
     */
    Page<Matchs> findAllByUser(Long user,Pageable pageable);

    /*
   查询老师编写的比赛
    */
    Page<Matchs> findByNameLike(String name,Pageable pageable);


}
