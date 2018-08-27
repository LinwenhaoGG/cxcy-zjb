package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Browse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrowseRepository extends JpaRepository<Browse,Long> {
    //通过用户id查找记录
    Browse findByUser(Long userId);
}
