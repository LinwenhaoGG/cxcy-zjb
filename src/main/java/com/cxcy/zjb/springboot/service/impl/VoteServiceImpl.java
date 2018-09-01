/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: VoteServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Vote;
import com.cxcy.zjb.springboot.repository.VoteRepository;
import com.cxcy.zjb.springboot.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Vote findById(Long id) {
        return voteRepository.findOne(id);
    }

    @Override
    @Transactional
    public void removeByVId(Long id) {
        voteRepository.delete(id);
    }

    @Override
    public Vote findByUser(Long uId) {
        return voteRepository.findByUser(uId);
    }

    @Override
    public Vote saveVote(Vote vote) {
        return voteRepository.save(vote);
    }
}