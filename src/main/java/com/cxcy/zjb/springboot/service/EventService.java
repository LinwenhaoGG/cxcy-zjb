/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: EventService
 * Author:   KOLO
 * Date:     2018/8/20 10:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface EventService {
    /**
     * 根据id获取项目
     * @param id
     * @return
     */
    Event getEventById(Long id);
    /**
     * 删除项目
     * @param id
     * @return
     */
    void removeEvent(Long id);

    /**
     * 通过项目负责人查找项目
     * @param uid
     * @return
     */
    List<Event> getEventByUser(Long uid, Pageable pageable);

    /**
     * 通过项目负责人查找项目的个数
     * @param uid
     * @return
     */
    Integer getEventCountByUid(Long uid);
}
