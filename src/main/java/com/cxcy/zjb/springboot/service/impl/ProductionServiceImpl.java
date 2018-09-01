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
import com.cxcy.zjb.springboot.service.*;
import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.repository.ProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
    @Autowired
    private CommentService commentService;

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
    public Production createComment(Long pId, String content,User user) {
        Production originalProduction = productionRepository.findOne(pId);
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        根据账号获取用户id
        Long uId = user.getId();
        Long sId = user.getStudent();
        Comment comment = new Comment(uId, content);//先创建一个评论
        commentService.saveComment(comment);

        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(sId);

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
        User user = userService.findUserById(uId);
        Long sId = user.getStudent();
        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(sId);

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
     * @param user
     */
    @Override
    public void createVoteOrRemoveVote(Long pId,User user) {
        Production originalProduction = productionRepository.findOne(pId);
        //获取一个用户id
        Long puId = originalProduction.getUser();
        //获取当前登录用户id
        Long uId = user.getId();
        //根据用户id查找对应的growth
        Growth growth = growthService.findByUser(userService.findUserById(puId).getStudent());
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Vote> votes = originalProduction.getVotes();
        Vote currentVote = null; // 当前用户的点赞情况
        for (Vote vote : votes) {
            if(uId.equals(vote.getUser())) {
                currentVote = vote;
                break;
            }
        }
        if(currentVote==null){
            currentVote = new Vote(uId);
            currentVote = voteService.saveVote(currentVote);
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
    public Page<Production> findProductionsByCategoryId(Long categoryId,Pageable pageable) {
     //   Page<Production> productions =productionRepository.findByCatagorysAndPCheck(categoryId,0,pageable);
        Page<Production> page = productionRepository.findByPCheck(0,pageable);
        return page;
    }

    @Override
    public Page<Production> findAll(Pageable pageable) {
        return productionRepository.findByPCheck(0,pageable);
    }

    @Override
    public Page<Production> findOrderByTimeDesc(Pageable pageable) {
        return productionRepository.findByPCheckOrderByPuploadTimeDesc(0,pageable);
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