/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: EventServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.repository.EventRepository;
import com.cxcy.zjb.springboot.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
@Service
public class EventServiceImpl implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event getEventById(Long id) {
        return eventRepository.getOne(id);
    }

    @Override
    public void removeEvent(Long id) {
        eventRepository.delete(id);
    }
}