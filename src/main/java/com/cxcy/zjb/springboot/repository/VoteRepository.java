package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Vote 仓库.
 *
 * @since 1.0.0 2017年4月9日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {
    /**
     * 根据uId查询对应的vote，判断是否存在
     * @param uId
     * @return
     */
    Vote findByUser(Long uId);
}
