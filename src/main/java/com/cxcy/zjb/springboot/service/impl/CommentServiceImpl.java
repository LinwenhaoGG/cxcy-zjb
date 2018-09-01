/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: CommentServiceImpl
 * Author:   KOLO
 * Date:     2018/8/20 10:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.cxcy.zjb.springboot.service.impl;

import com.cxcy.zjb.springboot.domain.Comment;
import com.cxcy.zjb.springboot.repository.CommentRepository;
import com.cxcy.zjb.springboot.service.CommentService;
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
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Override
    public Comment findByCId(Long cId) {
        return commentRepository.findOne(cId);
    }

    @Override
    public void removeCommentById(Long cId) {
        commentRepository.delete(cId);
    }

    @Override
    public Long findUserByCId(Long cId) {
        Comment comment = commentRepository.findById(cId);
        Long uId = comment.getUser();
        return uId;
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }
}