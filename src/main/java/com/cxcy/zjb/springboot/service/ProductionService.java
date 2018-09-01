/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ProductionService
 * Author:   KOLO
 * Date:     2018/8/20 10:41
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Comment;
import com.cxcy.zjb.springboot.domain.Production;

import java.util.List;

import com.cxcy.zjb.springboot.domain.Production;
import com.cxcy.zjb.springboot.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 学生业余作品的相关操作
 *
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface ProductionService {
    /**
     * 保存作品
     * @param production
     * @return
     */
    Production save(Production production);

    /**
     * 根据id查找对应的作品
     * @param pId
     * @return
     */
    Production findByPId(Long pId);

    /**
     * 根据pId删除作品
     * @param pId
     */
    void deleteByPId(Long pId);

    /**
     * 根据pId，每次点击浏览量就增加一次
     * @param pId
     * @return
     */
    int readingIncrease(Long pId);

    /**
     * 根据uId查找到所有的作品
     * @param user
     * @param pageable
     * @return
     */
    Page<Production> findByUserAndPCheck(Long user,Pageable pageable);


    /**
     * 创建评论
     * @param pId
     * @param pComment
     * @return
     */
    Production createComment(Long pId,String pComment,User user);


    /**
     * 删除评论
     * @param cId
     */
    void removeComment(Long pId,Long cId);

    /**
     * 取消或者点赞
     * @param pId
     * @param user
     */
    void createVoteOrRemoveVote(Long pId,User user);


    /**
     * 通过类型分页查找已审核通过的作品
     * @param categoryId
     * @return
     */
    public List<Production> findProductionsByCategoryId(Long categoryId);

    /**
     * 查询已审核通过的作品
     * @param sort
     * @return
     */
    public List<Production> findAll(Sort sort);

    /**
     * 按时间降序查找前10条已审核通过的作品
     * @return
     */
    public List<Production> findTop10orderByTimeDesc();

    /**
     * 按时间降序查找已审核通过的作品
     * @return
     */
    public List<Production> findOrderByTimeDesc();

    /**
     * 按分类查询前7条已审核作品
     * @param catagoryId
     * @return
     */
    public List<Production> findFirst7ByCatagorysAndPCheck(Long catagoryId, Sort sort);

    /**
     * 查询所有未通过审核的作品
     * @return
     */
    List<Production> findByPCheck(int i);

    /**
     * 用户查询自己所有的作品，包括未审核和审核不通过的
     * @param userId
     * @return
     */
    Page<Production> findAllByUserId(Long userId,Pageable pageable);

    /**
     * 查看分类下的作品
     * @param Cid
     * @return
     */
    List<Production> findByCid(Long Cid);


    /**
     * 通过标题模糊查询
     * @param ptitle
     * @return
     */
    Page<Production> findByPtitleLike(String ptitle,Pageable pageable);

    /**
     * 通过用户id模糊查询
     * @param ptitle
     * @return
     */
    Page<Production> findByUserIdLike(Long userId,Pageable pageable);
}
