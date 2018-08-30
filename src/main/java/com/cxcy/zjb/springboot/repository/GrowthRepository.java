package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Growth;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Growth 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface GrowthRepository extends JpaRepository<Growth,Long> {
    Growth findByUser(Long user);
}
