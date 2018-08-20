package com.cxcy.zjb.springboot.repository;

import com.cxcy.zjb.springboot.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *Student 仓库
 *
 * Created by LINWENHAO on 2018/8/6.
 */
public interface StudentRepository extends JpaRepository<Student,Long> {
}
