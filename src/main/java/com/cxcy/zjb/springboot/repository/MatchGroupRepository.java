package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.MatchGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *MatchGroup 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface MatchGroupRepository extends JpaRepository<MatchGroup,Long> {

    /**
     * 根据用户id查询报名的比赛列表
     * @param uid 用户id
     * @return
     */
    Page<MatchGroup> findByUser(Long uid, Pageable pageable);

    /**
     * 根据比赛项目id查找出队伍，并按成绩排名
     * @param eid
     * @return
     */
    List<MatchGroup> findByEventOrderByAchievementDesc(Long eid);
}
