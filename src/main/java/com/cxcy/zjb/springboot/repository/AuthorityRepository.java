package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Authority 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
