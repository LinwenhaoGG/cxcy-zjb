package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.MatchGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /**
     * 通过比赛项目id查找出队伍，进行评审
     * @param eventid
     * @return
     */
    @Query(value = "SELECT * FROM match_group " +
            "WHERE event_id = ?1 AND achievement =0.00 AND doc_address IS NOT NULL " +
            "LIMIT 0,10", nativeQuery = true)
    List<MatchGroup> findGroupPreview(Long eventid);

    /**
     * 通过比赛项目id查找出队伍，进行评审
     * @param eventid
     * @return
     */
    @Query(value = "SELECT count(*) FROM match_group " +
            "WHERE event_id = ?1 AND achievement =0.00 AND doc_address IS NOT NULL "
            , nativeQuery = true)
    int getPreviewGroupNum(Long eventid);
}
