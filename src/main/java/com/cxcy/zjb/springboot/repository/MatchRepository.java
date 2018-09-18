package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Matchs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Match 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface MatchRepository extends JpaRepository<Matchs,Long> {
    /**
     * 根据用户id查找该用户发布的比赛，分页
     * @param user
     * @param pageable
     * @return
     */
    Page<Matchs> findByUser(Long user, Pageable pageable);

    /**
     * 根据比赛名称查找
     * @param name
     * @param pageable
     * @return
     */
    Page<Matchs> findByNameLike(String name, Pageable pageable);
}
