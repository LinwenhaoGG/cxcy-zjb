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
     * 根据pId，每次点击就增加一次
     * @param pId
     * @return
     */
    int readingIncrease(Long pId);

    /**
     * 根据uId查找到所有的作品
     * @param user
     * @return
     */
    List<Production> findByUser(Long user);


    /**
     * 创建评论
     * @param pId
     * @param pComment
     * @return
     */
    Production createComment(Long pId,String pComment,String username);


    /**
     * 删除评论
     * @param cId
     */
    void removeComment(Long pId,Long cId);

    /**
     * 取消或者点赞
     * @param pId
     * @param username
     */
    void createVoteOrRemoveVote(Long pId,String username);

}
