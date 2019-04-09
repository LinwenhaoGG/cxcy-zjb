package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 *Enent 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface EventRepository extends JpaRepository<Event,Long> {

    /**
     * 根据负责人查找项目
     * @param uid
     * @return
     */
    @Query(value = "SELECT * FROM event WHERE id IN " +
            "(SELECT e_id FROM event_leader WHERE user_id =?1) " +
            "ORDER BY id DESC LIMIT ?2,?3 ",nativeQuery = true)
    public List<Event> getEventByUid(Long uid,Integer page,Integer size);

    /**
     * 根据负责人查找项目个数
     * @param uid

     * @return
     */
    @Query(value = "SELECT count(*) FROM event WHERE id IN " +
            "(SELECT e_id FROM event_leader WHERE user_id =?1)  ",nativeQuery = true)
    public Integer getEventCountByUid(Long uid);


}
