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

import com.cxcy.zjb.springboot.domain.Matchs;
import com.cxcy.zjb.springboot.repository.MatchRepository;
import com.cxcy.zjb.springboot.service.MatchService;
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
public class MatchServiceImpl implements MatchService {
    @Autowired
    private MatchRepository matchRepository;

    @Override
    public Matchs getMatchById(Long id) {
        return matchRepository.findOne(id);
    }

    @Override
    public Matchs saveMatch(Matchs matchs) {
        return matchRepository.save(matchs);
    }
}