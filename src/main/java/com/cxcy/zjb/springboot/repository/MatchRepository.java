package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Matchs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Match 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface MatchRepository extends JpaRepository<Matchs,Long> {
}
