/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CommentService
 * Author:   KOLO
 * Date:     2018/8/20 10:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service;

import com.cxcy.zjb.springboot.domain.Comment;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author KOLO
 * @create 2018/8/20
 * @since 1.0.0
 */
public interface CommentService {



    /**
     * 根据id获取相应的评论
     * @param cId
     * @return
     */
    Comment findByCId(Long cId);

    /**
     * 根据id移除评论
     * @param cId
     */
    void removeCommentById(Long cId);

    /**
     * 根据一个cId获取相应的uid
     * @param cId
     * @return
     */
    Long findUserByCId(Long cId);

    Comment saveComment(Comment comment);
}
