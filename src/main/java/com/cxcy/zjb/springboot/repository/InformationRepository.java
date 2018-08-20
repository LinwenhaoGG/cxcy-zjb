package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Information;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Information 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface InformationRepository extends JpaRepository<Information,Long> {
}
