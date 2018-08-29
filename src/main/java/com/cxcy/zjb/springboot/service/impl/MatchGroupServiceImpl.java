/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MatchGroupServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:40
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.MatchGroup;
import com.cxcy.zjb.springboot.repository.MatchGroupRepository;
import com.cxcy.zjb.springboot.service.MatchGroupService;
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
public class MatchGroupServiceImpl implements MatchGroupService {
    @Autowired
    private MatchGroupRepository matchGroupRepository;

    @Override
    public Page<MatchGroup> getMatchGroupByUid(Long uid, Pageable pageable) {
        return matchGroupRepository.findByUser(uid,pageable);
    }

    @Override
    public MatchGroup saveMatchGroup(MatchGroup matchGroup) {
        return matchGroupRepository.save(matchGroup);
    }
}