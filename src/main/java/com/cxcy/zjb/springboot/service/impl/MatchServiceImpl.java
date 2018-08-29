/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MatchServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Event;
import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.repository.MatchRepository;
import com.cxcy.zjb.springboot.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Override
    public void deleteMatchById(Long id) {
        matchRepository.delete(id);
    }


    @Override
    public Matchs createEvent(Long matchsId, Event event) {
        Matchs matchs = matchRepository.getOne(matchsId);
        if (event.getId() == null) { //判断是新增还是修改
            matchs.addEvent(event);
        } else {
            for (int index=0; index < matchs.getEventList().size(); index ++ ) {
                if (matchs.getEventList().get(index).getId().equals(event.getId())) {
                    matchs.getEventList().remove(index);
                    matchs.addEvent(event);
                    break;
                }
            }
        }
        return matchRepository.save(matchs);
    }

    @Override
    public void deleteEvent(Long matchsId, Long eventId) {
        Matchs matchs = matchRepository.getOne(matchsId);
        matchs.removeEvent(eventId);
        matchRepository.save(matchs);
    }

    @Override
    public Matchs getMatchById(Long id) {
        return matchRepository.findOne(id);
    }

    @Override
    public Page<Matchs> findAll(Pageable pageable) {

        return matchRepository.findAll(pageable);
    }

    @Override
    public Matchs saveMatch(Matchs matchs) {
        return matchRepository.save(matchs);
    }

    @Override
    public Page<Matchs> findAllByUser(Long user,Pageable pageable) {
        return matchRepository.findByUser(user,pageable);
    }
}