package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Comment 仓库.
 *
 * @since 1.0.0 2017年4月7日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * 根据cid查找评论
     * @param Id
     * @return
     */
    Comment findById(Long Id);
}
