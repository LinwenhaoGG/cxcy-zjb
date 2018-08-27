/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductionServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Comment;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.domain.User;
import com.cxcy.zjb.springboot.domain.Vote;
import com.cxcy.zjb.springboot.repository.ProductionRepository;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.service.VoteService;
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
public class ProductionServiceImpl implements ProductionService {

    @Autowired
    private ProductionRepository productionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VoteService voteService;

    @Override
    public Production save(Production production) {
        return productionRepository.save(production);
    }

    @Override
    public Production findByPId(Long pId) {
        return productionRepository.findOne(pId);
    }

    @Override
    public void deleteByPId(Long pId) {
        productionRepository.delete(pId);
    }

    @Override
    public int readingIncrease(Long pId) {
//        根据pId查找对应的作品
        Production production = productionRepository.findOne(pId);
        production.setReadSize(production.getReadSize()+1);
        productionRepository.save(production);
        return production.getReadSize();
    }



    @Override
    public List<Production> findByUser(Long user) {
        return productionRepository.findByUser(user);
    }

    @Override
    public Production createComment(Long pId, String pContent,String username) {
        Production originalProduction = productionRepository.findOne(pId);
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        根据账号名获取用户id
        User user = userService.findByUsername(username);
        Long uId = user.getId();
        Comment comment = new Comment(uId, pContent);//先创建一个评论
        originalProduction.addComment(comment);
        return productionRepository.save(originalProduction);
    }

    @Override
    public void removeComment(Long pId,Long cId) {
        Production originalProduction = productionRepository.findOne(pId);
        originalProduction.removeComment(cId);
        productionRepository.save(originalProduction);
    }
//  根据作品id和用户id，可以查出对应作品和用户
    @Override
    public void createVoteOrRemoveVote(Long pId,String username) {
        Production originalProduction = productionRepository.findOne(pId);
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Vote> votes = originalProduction.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况
        for (Vote vote : votes) {
            Long uId = userService.findByUId(vote.getUser()).getId();
            if(uId.equals(userService.findByUsername(username).getId())) {
                currentVote = vote;
                break;
            }
        }
        if(currentVote==null){
            User user = userService.findByUsername(username);
            Long uId = user.getId();
            currentVote = new Vote(uId);
            originalProduction.addVote(currentVote);
        }else{
            originalProduction.removeVote(currentVote.getVId());
            voteService.removeByVId(currentVote.getVId());
        }
        productionRepository.save(originalProduction);
    }

}