package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Company 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface CompanyRepository extends JpaRepository<Company,Long> {
}
