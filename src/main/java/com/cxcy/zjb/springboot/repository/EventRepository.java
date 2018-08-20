package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Enent 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface EventRepository extends JpaRepository<Event,Long> {
}
