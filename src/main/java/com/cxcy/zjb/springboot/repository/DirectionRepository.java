package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Direction;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

/**
 *Direction 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface DirectionRepository extends JpaRepository<Direction,Long> {
    /**
     * 查找所有的方向列表
     * @param pageable
     * @return
     */
    Page<Direction> findAll(Pageable pageable);
}
