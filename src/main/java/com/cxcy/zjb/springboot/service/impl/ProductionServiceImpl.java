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

import com.cxcy.zjb.springboot.domain.*;
import com.cxcy.zjb.springboot.repository.ProductionRepository;
import com.cxcy.zjb.springboot.service.GrowthService;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.repository.ProductionRepository;
import com.cxcy.zjb.springboot.service.ProductionService;
import com.cxcy.zjb.springboot.service.UserService;
import com.cxcy.zjb.springboot.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Autowired
    private GrowthService growthService;

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
        //获取一个用户id
        Long uId = production.getUser();
        //根据uid获取sid
        User user = userService.findUserById(uId);
        Long sId = user.getStudent();
        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(sId);

        //添加相应的gid的浏览量+1
        growth.setGReadSize(growth.getGReadSize()+1);
        //保存更新新的数据
        growthService.save(growth);

        productionRepository.save(production);
        return production.getReadSize();
    }



    @Override
    public Page<Production> findByUserAndPCheck(Long user,Pageable pageable) {
        return productionRepository.findByUserAndPCheck(user,1,pageable);
    }

    @Override
    public Production createComment(Long pId, String pContent,String username) {
        Production originalProduction = productionRepository.findOne(pId);
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        根据账号名获取用户id
        User user = userService.findByUsername(username);
        Long uId = user.getId();
        Comment comment = new Comment(uId, pContent);//先创建一个评论

        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(uId);

        //添加相应的gid的评论量+1
        growth.setGReadSize(growth.getGComment()+1);
        //保存更新新的数据
        growthService.save(growth);
        originalProduction.addComment(comment);

        return productionRepository.save(originalProduction);
    }

    @Override
    public void removeComment(Long pId,Long cId) {
        Production originalProduction = productionRepository.findOne(pId);
        //获取一个用户id
        Long uId = originalProduction.getUser();
        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(uId);

        //减少相应的gid的评论量-1
        growth.setGReadSize(growth.getGComment()-1);
        //保存更新新的数据
        growthService.save(growth);
        originalProduction.removeComment(cId);
        productionRepository.save(originalProduction);
    }
//  根据作品id和用户id，可以查出对应作品和用户

    /**
     * 点赞或者取消赞
     * @param pId
     * @param username
     */
    @Override
    public void createVoteOrRemoveVote(Long pId,String username) {
        Production originalProduction = productionRepository.findOne(pId);
        //获取一个用户id
        Long puId = originalProduction.getUser();
        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(puId);
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Vote> votes = originalProduction.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况
        for (Vote vote : votes) {
            Long uId = userService.findUserById(vote.getUser()).getId();
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
            growth.setGVote(growth.getGVote()+1);
        }else {
            originalProduction.removeVote(currentVote.getVId());
            growth.setGVote(growth.getGVote() - 1);
            voteService.removeByVId(currentVote.getVId());
        }
        productionRepository.save(originalProduction);
        growthService.save(growth);
    }


    @Override
    public List<Production> findProductionsByCategoryId(Long categoryId) {
       List<Production> productions =productionRepository.findByCatagorysAndPCheck(categoryId,0);
       // Page<Production> list = productionRepository.findByPCheck(0);
        return productions;
    }

    @Override
    public List<Production> findAll(Sort sort) {
        return productionRepository.findTop10ByPCheck(0,sort);
    }

    @Override
    public List<Production> findTop10orderByTimeDesc() {
        return productionRepository.findTop10ByPCheckOrderByPuploadTimeDesc(0);
    }


    @Override
    public List<Production> findOrderByTimeDesc() {
        return productionRepository.findByPCheckOrderByPuploadTimeDesc(0);
    }

    @Override
    public List<Production> findFirst7ByCatagorysAndPCheck(Long catagoryId, Sort sort) {
        return productionRepository.findFirst7ByCatagorysAndPCheck(catagoryId,0,sort);
    }

    @Override
    public List<Production> findByPCheck(int i) {
        return productionRepository.findByPCheck(0);
    }
}