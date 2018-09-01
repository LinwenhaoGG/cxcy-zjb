package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Catagorys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Catalog 仓库.
 *
 * @since 1.0.0 2017年4月10日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
public interface CatagoryRepository extends JpaRepository<Catagorys, Long> {
    /**
     * 根据方向ID查出对应的分类
     * @param direction
     * @return
     */
    List<Catagorys> findByDirection(Long direction);




}
